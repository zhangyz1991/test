package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.cglib.Callback;
import com.vick.test.util.jdbc.cglib.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface MethodInterceptor extends Callback {
    Object intercept(Object var1, Method var2, Object[] var3, MethodProxy var4) throws Throwable;
}
