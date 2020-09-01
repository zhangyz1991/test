package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface LazyLoader extends Callback {
    Object loadObject() throws Exception;
}
