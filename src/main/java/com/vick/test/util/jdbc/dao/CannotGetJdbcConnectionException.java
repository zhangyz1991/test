package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

import java.sql.SQLException;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class CannotGetJdbcConnectionException extends DataAccessResourceFailureException {
    public CannotGetJdbcConnectionException(String msg) {
        super(msg);
    }

    public CannotGetJdbcConnectionException(String msg, @Nullable SQLException ex) {
        super(msg, ex);
    }
}
