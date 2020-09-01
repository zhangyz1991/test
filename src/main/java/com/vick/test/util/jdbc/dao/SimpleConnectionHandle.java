package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Assert;

import java.sql.Connection;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class SimpleConnectionHandle implements ConnectionHandle {

    private final Connection connection;


    /**
     * Create a new SimpleConnectionHandle for the given Connection.
     * @param connection the JDBC Connection
     */
    public SimpleConnectionHandle(Connection connection) {
        Assert.notNull(connection, "Connection must not be null");
        this.connection = connection;
    }

    /**
     * Return the specified Connection as-is.
     */
    @Override
    public Connection getConnection() {
        return this.connection;
    }


    @Override
    public String toString() {
        return "SimpleConnectionHandle: " + this.connection;
    }

}
