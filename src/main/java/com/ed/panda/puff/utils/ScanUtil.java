package com.ed.panda.puff.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Edward
 * @Date: 2019/12/3 上午10:42
 */
public class ScanUtil {

    /**
     * 扫描指定路径下所有class
     *
     * @param packagePath
     */
    public static List<Class<?>> scanClass(String packagePath) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            Resource[] resources = resourcePatternResolver.getResources("classpath*:"
                    + packagePath.replaceAll("[.]", "/") + "/**/*.class");
            // 把每一个class文件找出来
            for (Resource r : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(r);
                Class<?> clazz = ClassUtils.forName(metadataReader.getClassMetadata().getClassName(), null);
                classes.add(clazz);
            }
        } catch (Exception e) {
            throw new RuntimeException("scan path: " + packagePath + " fail", e);
        }
        return classes;
    }

    /**
     * 扫描指定路径下，被指定注解修饰的method
     *
     * @param basePackages
     * @param annotationClass
     */
    public static List<Method> scanMethodsWithAnnotation(String[] basePackages, Class<? extends Annotation> annotationClass) {
        List<Class<?>> allClass = new ArrayList<>();
        Arrays.stream(basePackages).forEach(packagePath -> allClass.addAll(scanClass(packagePath)));
        List<Method> methods = new ArrayList<>();
        allClass.forEach(clazz -> {
            List<Method> subMethods = Arrays.stream(clazz.getMethods()).filter(m -> m.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
            methods.addAll(subMethods);
        });
        return methods;
    }
}
