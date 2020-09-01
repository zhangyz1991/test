package com.vick.test.util.jdbc.dao;

import java.sql.SQLWarning;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class SQLWarningException extends UncategorizedDataAccessException {

    /**
     * Constructor for SQLWarningException.
     * @param msg the detail message
     * @param ex the JDBC warning
     */
    public SQLWarningException(String msg, SQLWarning ex) {
        super(msg, ex);
    }

    /**
     * Return the underlying SQLWarning.
     */
    public SQLWarning SQLWarning() {
        return (SQLWarning) getCause();
    }

}

