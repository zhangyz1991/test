package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@FunctionalInterface
public interface ObjectFactory<T> {

    /**
     * Return an instance (possibly shared or independent)
     * of the object managed by this factory.
     * @return the resulting instance
     * @throws BeansException in case of creation errors
     */
    T getObject() throws BeansException;

}
