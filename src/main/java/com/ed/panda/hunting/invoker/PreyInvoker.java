package com.ed.panda.hunting.invoker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ed.panda.hunting.common.MsgLogStatus;
import com.ed.panda.hunting.context.HuntingApplicationContext;
import com.ed.panda.hunting.context.HuntingContext;
import com.ed.panda.hunting.context.PreyInfo;
import com.ed.panda.hunting.repository.ILocalMsgLogRepository;
import com.ed.panda.hunting.repository.IRetryStrategy;
import com.ed.panda.hunting.repository.LocalMsgLog;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Edward
 * @date : 2020/11/2 下午7:45
 */
@Slf4j
@Setter
public class PreyInvoker {

    private HuntingApplicationContext context;

    private ILocalMsgLogRepository repository;

    //全局线程池
    private Executor global = new ThreadPoolExecutor(10, 50, 10, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.DiscardPolicy());

    public boolean invoke(LocalMsgLog msgLog) {
        MsgLogStatus status = MsgLogStatus.FAIL;
        //获取元数据
        PreyInfo preyInfo = HuntingContext.getPreyInfo(msgLog.getBizType());
        Assert.notNull(preyInfo, "no preyInfo in container,bizType=" + msgLog.getBizType());
        try {
            //get方法参数/调用对象
            JSONArray params = JSON.parseArray(msgLog.getParams());
            Parameter[] parameters = preyInfo.getMethod().getParameters();
            handleParamsType(params, parameters);
            Object target = context.getBean(preyInfo.getTargetClass());
            //反射调用,真正执行方法
            preyInfo.getMethod().invoke(target, params.toArray());
            status = MsgLogStatus.SUCCESS;
            return true;
        } catch (Exception e) {
            log.error("preyInvoke失败,msgLog={}", msgLog, e);
            return false;
        } finally {
            Date nextRetryTime = msgLog.getNextRetryTime();
            if (status == MsgLogStatus.FAIL) {
                //获取重试策略,计算重试时间
                String beanName = HuntingContext.strategys.getOrDefault(msgLog.getBizType(), "defaultRetryStrategy");
                IRetryStrategy retryStrategy = context.getBeanName(IRetryStrategy.class, beanName);
                nextRetryTime = retryStrategy.calculateNextRetryTime(msgLog);
            }
            //更新localMsgLog
            repository.updateLocalMsgLog(msgLog.getId(), msgLog.getRetryCount() + 1, nextRetryTime, status.getStatus());
        }
    }

    public void asyncInvoke(LocalMsgLog msgLog) {
        Executor executor = null;
        String executorName = HuntingContext.executors.get(msgLog.getBizType());
        if (!StringUtils.isEmpty(executorName)) {
            executor = context.getBeanName(Executor.class, executorName);
        }
        if (executor == null) {
            executor = global;
        }
        executor.execute(() -> invoke(msgLog));
    }

    private void handleParamsType(JSONArray params, Parameter[] parameters) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < params.size(); i++) {
            Class<?> type = parameters[i].getType();
            Class<?> clazz = params.get(i).getClass();
            if (JSONArray.class.isAssignableFrom(clazz)) {
                params.set(i, params.getJSONArray(i).toJavaObject(type));
                continue;
            }
            if (JSONObject.class.isAssignableFrom(clazz)) {
                params.set(i, params.getJSONObject(i).toJavaObject(type));
                continue;
            }
            //基本类型,都转为封装类
            if (type.isPrimitive()) {
                type = ClassUtils.resolvePrimitiveIfNecessary(type);
            }
            //包装类&&类型不一致,先转成字符串再valueOf
            if (ClassUtils.isPrimitiveWrapper(type)) {
                if (!type.equals(params.get(i).getClass())) {
                    Method method = type.getMethod("valueOf", String.class);
                    Object val = method.invoke(null, String.valueOf(params.get(i)));
                    params.set(i, val);
                }
            }
        }
    }
}
