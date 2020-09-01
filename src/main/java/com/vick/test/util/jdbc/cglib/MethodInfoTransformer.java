package com.vick.test.util.jdbc.cglib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class MethodInfoTransformer implements Transformer {
    private static final MethodInfoTransformer INSTANCE = new MethodInfoTransformer();

    public MethodInfoTransformer() {
    }

    public static MethodInfoTransformer getInstance() {
        return INSTANCE;
    }

    public Object transform(Object value) {
        if (value instanceof Method) {
            return ReflectUtils.getMethodInfo((Method)value);
        } else if (value instanceof Constructor) {
            return ReflectUtils.getMethodInfo((Constructor)value);
        } else {
            throw new IllegalArgumentException("cannot get method info for " + value);
        }
    }
}
