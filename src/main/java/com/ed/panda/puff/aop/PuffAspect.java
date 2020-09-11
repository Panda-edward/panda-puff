package com.ed.panda.puff.aop;


import com.alibaba.fastjson.JSONArray;
import com.ed.panda.puff.anno.Puff;
import com.ed.panda.puff.common.PuffRuntimeException;
import com.ed.panda.puff.context.PuffContext;
import com.ed.panda.puff.context.PuffMeta;
import com.ed.panda.puff.invoker.PuffInvoker;
import com.ed.panda.puff.repository.ITransLogRepository;
import com.ed.panda.puff.repository.PuffTransLog;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/09/06 下午4:31
 *
 * <p>
 * 拦截@Puff修饰方法,把同步分布式调用->本地事务+异步分布式调用
 */
@Aspect
@Setter
public class PuffAspect {

    private PuffInvoker puffInvoker;

    private PuffContext puffContext;

    private ITransLogRepository transLogRepository;

    /**
     * 切点:所有@Puff修饰的方法
     * <p>
     * 1.获取调用方法的aop基本信息，封装record
     * 2.同步事务中，将record记录插入本地数据库
     * 3.注册同步事务处理器：afterCommit 执行真是的分布式业务
     */
    @Around(value = "@annotation(com.ed.panda.puff.anno.Puff)")
    public void txRecord(ProceedingJoinPoint joinPoint) {
        //如果当前线程不在事务中，抛出异常
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new PuffRuntimeException("no transaction in current require");
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //build txLog
        Puff puff = method.getAnnotation(Puff.class);
        PuffMeta meta = puffContext.getMetadataCache().get(puff.bizType());
        Assert.notNull(meta, puff.bizType() + "->对应PuffMeta不存在");
        Object[] args = joinPoint.getArgs();
        String puffBizId = String.valueOf(args[meta.getBizIdIndex()]);
        PuffTransLog transLog = new PuffTransLog().setPuffBizId(puffBizId).setBizType(puff.bizType())
                .setParams(JSONArray.toJSONString(args)).setRetryCount(0).setNextRetryTime(new Date());
        //本地DB save
        boolean ret = transLogRepository.saveTransLog(transLog);
        if (!ret) {
            throw new PuffRuntimeException("to db,save local trans log fail");
        }
        //注册事务触发事件
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                puffInvoker.asyncInvoke(transLog);
            }
        });
    }
}
