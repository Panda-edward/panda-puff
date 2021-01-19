package com.ed.panda.hunting.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author : Edward
 * @date : 2020/11/2 下午7:54
 */
public class HuntingApplicationContext implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public <T> T getBeanName(Class<T> beanClass, String beanName) {
        Map<String, T> beansOfType = context.getBeansOfType(beanClass);
        return beansOfType.get(beanName);
    }

}
