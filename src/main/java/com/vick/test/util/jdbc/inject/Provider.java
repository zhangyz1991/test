package com.vick.test.util.jdbc.inject;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface Provider<T> {
    T get();
}
