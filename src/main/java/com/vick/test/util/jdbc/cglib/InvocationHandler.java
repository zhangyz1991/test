package com.vick.test.util.jdbc.cglib;

import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface InvocationHandler extends Callback {
    Object invoke(Object var1, Method var2, Object[] var3) throws Throwable;
}
