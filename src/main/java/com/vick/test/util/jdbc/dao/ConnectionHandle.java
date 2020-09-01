package com.vick.test.util.jdbc.dao;

import java.sql.Connection;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@FunctionalInterface
public interface ConnectionHandle {

    /**
     * Fetch the JDBC Connection that this handle refers to.
     */
    Connection getConnection();

    /**
     * Release the JDBC Connection that this handle refers to.
     * <p>The default implementation is empty, assuming that the lifecycle
     * of the connection is managed externally.
     * @param con the JDBC Connection to release
     */
    default void releaseConnection(Connection con) {
    }

}
