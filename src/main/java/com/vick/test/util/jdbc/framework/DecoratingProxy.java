package com.vick.test.util.jdbc.framework;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface DecoratingProxy {

    /**
     * Return the (ultimate) decorated class behind this proxy.
     * <p>In case of an AOP proxy, this will be the ultimate target class,
     * not just the immediate target (in case of multiple nested proxies).
     * @return the decorated class (never {@code null})
     */
    Class<?> getDecoratedClass();

}
