package com.ed.panda.puff.config;

import com.ed.panda.puff.aop.PuffAspect;
import com.ed.panda.puff.context.PuffContext;
import com.ed.panda.puff.invoker.PuffInvoker;
import com.ed.panda.puff.repository.ITransLogRepository;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author : Edward
 * @date : 2020/9/10 上午12:07
 *
 * <p>
 * 向spring容器注册puff组件的Bean
 */
@Configuration
@ConditionalOnClass(TransactionManager.class)
@EnableConfigurationProperties(PuffProperties.class)
@Setter
public class PuffAutoConfiguration implements InitializingBean {

    private PuffContext context;

    private ITransLogRepository transLogRepository;

    private PuffProperties properties;

    public PuffAutoConfiguration(PuffContext context, PuffProperties properties, ITransLogRepository transLogRepository) {
        this.context = context;
        this.properties = properties;
        this.transLogRepository = transLogRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //根据配置,设置puffContext
        addAttrs4PuffContext();
    }

    private void addAttrs4PuffContext() {
        PuffProperties.ExecutorConfig config = properties.getGlobalExecutor();
        Executor globalPool = buildThreadPool(config,true);
        Map<Integer, Executor> bizPools = new HashMap<>(8);
        if (properties.getBizExecutor() != null) {
            properties.getBizExecutor().forEach((k, v) -> {
                bizPools.put(k, buildThreadPool(v,false));
            });
        }
        context.setGlobalThreadPool(globalPool);
        context.setBizThreadPools(bizPools);
    }

    @Bean
    public PuffInvoker puffInvoker() {
        PuffInvoker invoker = new PuffInvoker();
        invoker.setPuffContext(context);
        invoker.setTransLogRepository(transLogRepository);
        return invoker;
    }

    @Bean
    public PuffAspect puffAspect(PuffInvoker invoker) {
        PuffAspect puffAspect = new PuffAspect();
        puffAspect.setPuffContext(context);
        puffAspect.setPuffInvoker(invoker);
        puffAspect.setTransLogRepository(transLogRepository);
        return puffAspect;
    }

    private Executor buildThreadPool(PuffProperties.ExecutorConfig config,boolean global) {
        int poolSize;
        if (config == null) {
            if (!global) {
                return null;
            }
            poolSize = 50;
        }else {
            poolSize = config.getPoolSize();
        }
        return Executors.newFixedThreadPool(poolSize);
    }
}
