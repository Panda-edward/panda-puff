package com.ed.panda.hunting.context;

import com.ed.panda.hunting.repository.IRetryStrategy;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Edward
 * @date : 2020/11/2 下午7:03
 */
@Accessors(chain = true)
@Data
public class PreyInfo {

    private Class<?> targetClass;

    private Method method;

    /**
     * 业务主键在参数中的位置
     */
    private int bizIdx;

}
