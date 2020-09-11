package com.ed.panda.puff.context;

import com.ed.panda.puff.anno.Puff;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author : Edward
 * @date : 2020/9/10 下午3:27
 */
@Deprecated
public class ScanComponent implements EnvironmentAware, ResourceLoaderAware {

    private Environment environment;

    private ResourceLoader resourceLoader;

    /**
     * 扫描指定类
     */
    private Set<BeanDefinition> scan(String path, Class<? extends Annotation> annotationClass) {
        if (path == null) {
            return Collections.emptySet();
        }
        //
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true, environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().hasAnnotatedMethods(Puff.class.getName())) {
                    return true;
                }
                return false;
            }
        };
        scanner.setResourceLoader(resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(annotationClass);
        scanner.addIncludeFilter(annotationTypeFilter);
        return scanner.findCandidateComponents(path);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
