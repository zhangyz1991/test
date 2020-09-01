package com.vick.test.util.jdbc.beans;

import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface MethodReplacer {

    /**
     * Reimplement the given method.
     * @param obj the instance we're reimplementing the method for
     * @param method the method to reimplement
     * @param args arguments to the method
     * @return return value for the method
     */
    Object reimplement(Object obj, Method method, Object[] args) throws Throwable;

}
