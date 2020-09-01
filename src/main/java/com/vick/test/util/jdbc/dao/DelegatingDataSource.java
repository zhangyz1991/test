package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.beans.InitializingBean;
import com.vick.test.util.jdbc.framework.Nullable;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DelegatingDataSource implements DataSource, InitializingBean {

    @Nullable
    private DataSource targetDataSource;


    /**
     * Create a new DelegatingDataSource.
     * @see #setTargetDataSource
     */
    public DelegatingDataSource() {
    }

    /**
     * Create a new DelegatingDataSource.
     * @param targetDataSource the target DataSource
     */
    public DelegatingDataSource(DataSource targetDataSource) {
        setTargetDataSource(targetDataSource);
    }


    /**
     * Set the target DataSource that this DataSource should delegate to.
     */
    public void setTargetDataSource(@Nullable DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    /**
     * Return the target DataSource that this DataSource should delegate to.
     */
    @Nullable
    public DataSource getTargetDataSource() {
        return this.targetDataSource;
    }

    /**
     * Obtain the target {@code DataSource} for actual use (never {@code null}).
     * @since 5.0
     */
    protected DataSource obtainTargetDataSource() {
        DataSource dataSource = getTargetDataSource();
        Assert.state(dataSource != null, "No 'targetDataSource' set");
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        if (getTargetDataSource() == null) {
            throw new IllegalArgumentException("Property 'targetDataSource' is required");
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        return obtainTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return obtainTargetDataSource().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return obtainTargetDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        obtainTargetDataSource().setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return obtainTargetDataSource().getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        obtainTargetDataSource().setLoginTimeout(seconds);
    }


    //---------------------------------------------------------------------
    // Implementation of JDBC 4.0's Wrapper interface
    //---------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return obtainTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || obtainTargetDataSource().isWrapperFor(iface));
    }


    //---------------------------------------------------------------------
    // Implementation of JDBC 4.1's getParentLogger method
    //---------------------------------------------------------------------

    @Override
    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

}
