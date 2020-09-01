package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

import java.sql.SQLException;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class UncategorizedSQLException extends UncategorizedDataAccessException {

    /** SQL that led to the problem. */
    @Nullable
    private final String sql;


    /**
     * Constructor for UncategorizedSQLException.
     * @param task name of current task
     * @param sql the offending SQL statement
     * @param ex the root cause
     */
    public UncategorizedSQLException(String task, @Nullable String sql, SQLException ex) {
        super(task + "; uncategorized SQLException" + (sql != null ? " for SQL [" + sql + "]" : "") +
                "; SQL state [" + ex.getSQLState() + "]; error code [" + ex.getErrorCode() + "]; " +
                ex.getMessage(), ex);
        this.sql = sql;
    }


    /**
     * Return the underlying SQLException.
     */
    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    /**
     * Return the SQL that led to the problem (if known).
     */
    @Nullable
    public String getSql() {
        return this.sql;
    }

}
