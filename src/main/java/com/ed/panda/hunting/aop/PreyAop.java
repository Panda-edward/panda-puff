package com.ed.panda.hunting.aop;

import com.alibaba.fastjson.JSONArray;
import com.ed.panda.hunting.anno.BizSerialNo;
import com.ed.panda.hunting.common.MsgLogStatus;
import com.ed.panda.hunting.context.HunterInfo;
import com.ed.panda.hunting.context.HuntingContext;
import com.ed.panda.hunting.context.PreyInfo;
import com.ed.panda.hunting.invoker.PreyInvoker;
import com.ed.panda.hunting.repository.ILocalMsgLogRepository;
import com.ed.panda.hunting.repository.LocalMsgLog;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午6:04
 */
@Aspect
@Setter
public class PreyAop {

    private ILocalMsgLogRepository repository;

    private PreyInvoker preyInvoker;

    @Around(value = "@annotation(com.ed.panda.hunting.anno.Prey)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HunterInfo hunterInfo = HuntingContext.getHunterInfo();
        //hunterInfo不存在,代表是重试进入逻辑,直接调用Bean
        //否则,进入捕获逻辑
        if (hunterInfo == null) {
            return joinPoint.proceed(joinPoint.getArgs());
        } else {
            capture(joinPoint);
        }
        return null;
    }

    private void capture(ProceedingJoinPoint joinPoint) {
        //当前线程不在事务中，抛出异常
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new RuntimeException("no transaction in current request");
        }
        HunterInfo hunterInfo = HuntingContext.getHunterInfo();
        Assert.notNull(hunterInfo, "no hunter info in current request");
        PreyInfo preyInfo = HuntingContext.getPreyInfo(hunterInfo.getBizType());
        //init preyInfo
        if (preyInfo == null) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            int bizIdx = getBizIdx(method);
            preyInfo = new PreyInfo().setTargetClass(method.getDeclaringClass()).setMethod(method).setBizIdx(bizIdx);
            HuntingContext.setPreyInfo(hunterInfo.getBizType(), preyInfo);
        }
        //build localMsgLog
        Object[] args = joinPoint.getArgs();
        String bizSerialNo = preyInfo.getBizIdx() < 0 ? "" : String.valueOf(args[preyInfo.getBizIdx()]);
        LocalMsgLog localMsgLog = new LocalMsgLog()
                .setBizType(hunterInfo.getBizType())
                .setBizSerialNo(bizSerialNo)
                .setParams(JSONArray.toJSONString(args))
                .setStatus(MsgLogStatus.INIT.getStatus())
                .setRetryCount(0)
                .setNextRetryTime(new Date());
        //本地DB save
        boolean ret = repository.saveLocalMsgLog(localMsgLog);
        if (!ret) {
            throw new RuntimeException("to db,save local tx log fail");
        }
        //注册事务钩子函数:提交后,invoke真实调用
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
//                preyInvoker.asyncInvoke(localMsgLog); todo
                preyInvoker.invoke(localMsgLog);
            }
        });
    }

    private int getBizIdx(Method m) {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(BizSerialNo.class)) {
                return i;
            }
        }
        return -1;
    }


}
