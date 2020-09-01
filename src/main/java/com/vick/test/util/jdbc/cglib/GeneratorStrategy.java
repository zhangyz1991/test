package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface GeneratorStrategy {
    byte[] generate(ClassGenerator var1) throws Exception;

    boolean equals(Object var1);
}
