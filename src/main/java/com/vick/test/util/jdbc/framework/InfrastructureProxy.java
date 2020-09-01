package com.vick.test.util.jdbc.framework;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface InfrastructureProxy {

    /**
     * Return the underlying resource (never {@code null}).
     */
    Object getWrappedObject();

}
