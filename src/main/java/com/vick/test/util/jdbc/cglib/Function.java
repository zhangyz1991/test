package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface Function<K, V> {
    V apply(K var1);
}
