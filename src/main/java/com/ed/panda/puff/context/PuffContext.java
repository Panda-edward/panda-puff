package com.ed.panda.puff.context;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author : Edward
 * @date : 2020/9/8 下午4:02
 */
@Data
public class PuffContext implements ApplicationContextAware {

    private ApplicationContext context;

    /**
     * 全局线程池
     * */
    private Executor globalThreadPool;

    /**
     * 每个bizType任务对应的线程池,可以不设置
     * */
    private Map<Integer, Executor> bizThreadPools;

    /**
     * puff元数据
     * */
    private Map<Integer, PuffMeta> metadataCache;

    public <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
