package com.ed.panda.puff.config;

import com.ed.panda.puff.anno.EnablePuff;
import com.ed.panda.puff.anno.Puff;
import com.ed.panda.puff.anno.PuffBizId;
import com.ed.panda.puff.common.PuffLoadException;
import com.ed.panda.puff.context.PuffContext;
import com.ed.panda.puff.context.PuffMeta;
import com.ed.panda.puff.utils.ScanUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Edward
 * @date : 2020/9/7 下午11:16
 */
public class PuffScannerRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //获取EnablePuff注解属性
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnablePuff.class.getName());
        if (annotationAttributes == null || annotationAttributes.size() == 0) {
            return;
        }
        boolean enableRetry = (Boolean) annotationAttributes.get("enableRetry");
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");
        //扫描相关类
        List<Method> puffMethod = ScanUtil.scanMethodsWithAnnotation(basePackages, Puff.class);
        //封装puff元数据对象
        Map<Integer, PuffMeta> metadataCache = puffMethod.stream().map(method -> {
            int bizIdIndex = getPuffBizIdIndex(method);
            int bizType = method.getAnnotation(Puff.class).bizType();
            return new PuffMeta().setTargetClass(method.getDeclaringClass()).setMethod(method).setBizIdIndex(bizIdIndex).setBizType(bizType);
        }).collect(Collectors.toMap(PuffMeta::getBizType, meta -> meta));
        //注册puffContext BeanDefinition
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(PuffContext.class);
        builder.addPropertyValue("metadataCache", metadataCache);
        registry.registerBeanDefinition("puffContext", builder.getBeanDefinition());
    }

    private int getPuffBizIdIndex(Method m) {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PuffBizId.class)) {
                return i;
            }
        }
        throw new PuffLoadException("no @PuffBizId on method`s params");
    }

}
