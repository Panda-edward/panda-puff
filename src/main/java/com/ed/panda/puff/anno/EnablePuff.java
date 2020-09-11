package com.ed.panda.puff.anno;

import com.ed.panda.puff.config.PuffScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Edward
 * @date : 2020/09/07 下午3:51
 *
 * <p>
 * 启用puff组件
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(PuffScannerRegistrar.class)
public @interface EnablePuff {

    /**
     * 扫描路径
     */
    String[] basePackages();

    /**
     * 是否启用内置重试任务:推荐使用统一的调度平台进行重试
     */
    boolean enableRetry() default false;
}
