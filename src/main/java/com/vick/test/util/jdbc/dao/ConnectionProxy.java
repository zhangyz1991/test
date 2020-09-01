package com.vick.test.util.jdbc.dao;

import java.sql.Connection;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface ConnectionProxy extends Connection {

    /**
     * Return the target Connection of this proxy.
     * <p>This will typically be the native driver Connection
     * or a wrapper from a connection pool.
     * @return the underlying Connection (never {@code null})
     */
    Connection getTargetConnection();

}
