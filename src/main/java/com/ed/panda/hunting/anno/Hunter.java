package com.ed.panda.hunting.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Edward
 * @date : 2020/11/2 下午5:20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Hunter {

    String bizType();

    String retryStrategy() default "";

    String executorName() default "";
}
