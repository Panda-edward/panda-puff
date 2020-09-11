package com.ed.panda.puff.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @author : Edward
 * @date : 2020/9/8 下午4:04
 */
@Data
@Accessors(chain = true)
public class PuffMeta {

    private Class<?> targetClass;

    private Method method;

    /**
     * 业务主键,参数中的位置
     * */
    private int bizIdIndex;

    private int bizType;
}
