package com.ed.panda.puff.invoker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ed.panda.puff.common.PuffRuntimeException;
import com.ed.panda.puff.common.enums.TransLogStatusEnum;
import com.ed.panda.puff.context.PuffContext;
import com.ed.panda.puff.context.PuffMeta;
import com.ed.panda.puff.repository.ITransLogRepository;
import com.ed.panda.puff.repository.PuffTransLog;
import com.ed.panda.puff.utils.ProxyBeanUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.Executor;

/**
 * @author : Edward
 * @date : 2020/9/8 下午8:12
 */
@Slf4j
@Setter
public class PuffInvoker {

    private PuffContext puffContext;

    private ITransLogRepository transLogRepository;

    public boolean invoke(PuffTransLog record) {
        TransLogStatusEnum status = TransLogStatusEnum.FAIL;
        try {
            //获取元数据
            PuffMeta meta = puffContext.getMetadataCache().get(record.getBizType());
            if (meta == null) {
                throw new PuffRuntimeException(record.getBizType() + "->对应PuffMeta不存在");
            }
            //get方法参数/调用对象
            JSONArray params = JSON.parseArray(record.getParams());
            Parameter[] parameters = meta.getMethod().getParameters();
            handleParamsType(params, parameters);
            Object target = ProxyBeanUtil.getTarget(puffContext.getBean(meta.getTargetClass()));
            //反射调用,真正执行方法
            meta.getMethod().invoke(target, params.toArray());
            status = TransLogStatusEnum.SUCCESS;
            return true;
        } catch (Exception e) {
            log.error("puff-invoke失败,record={}", record, e);
            return false;
        } finally {
            //更新TransLog
            transLogRepository.updateTransLog(record.getPuffBizId(), status.getStatus(), record.getStatus(),
                    record.getRetryCount() + 1, transLogRepository.calculateNextRetryTime(record));
        }
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

    public void asyncInvoke(PuffTransLog record) {
        Executor executor = puffContext.getBizThreadPools().get(record.getBizType());
        if (executor == null) {
            executor = puffContext.getGlobalThreadPool();
        }
        executor.execute(() -> invoke(record));
    }
}
