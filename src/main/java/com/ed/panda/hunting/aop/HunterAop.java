package com.ed.panda.hunting.aop;

import com.ed.panda.hunting.anno.Hunter;
import com.ed.panda.hunting.context.HunterInfo;
import com.ed.panda.hunting.context.HuntingContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author : Edward
 * @date : 2020/11/2 下午5:22
 */
@Aspect
public class HunterAop {


    @Around(value = "@annotation(com.ed.panda.hunting.anno.Hunter)")
    public void ambush(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //获取Hunter信息
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Hunter hunter = method.getAnnotation(Hunter.class);
            String bizType = hunter.bizType();
            String retryStrategy = hunter.retryStrategy();
            String executorName = hunter.executorName();
            //init huntingContext
            if (!StringUtils.isEmpty(retryStrategy) && !HuntingContext.strategys.containsKey(bizType)) {
                HuntingContext.strategys.put(bizType, retryStrategy);
            }
            if (!StringUtils.isEmpty(executorName) && !HuntingContext.executors.containsKey(bizType)) {
                HuntingContext.executors.put(bizType, executorName);
            }
            //存入threadLocal
            HunterInfo hunterInfo = new HunterInfo().setBizType(bizType);
            HuntingContext.setHunterInfo(hunterInfo);
            //继续调用
            joinPoint.proceed(joinPoint.getArgs());
        } finally {
            HuntingContext.clear();
        }
    }
}
