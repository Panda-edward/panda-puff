package com.ed.panda.puff.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Edward
 * @date : 2020/9/7 下午11:06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Puff {

    /**
     * 业务类型
     */
    int bizType();
}
