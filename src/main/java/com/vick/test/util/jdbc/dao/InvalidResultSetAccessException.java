package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

import java.sql.SQLException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class InvalidResultSetAccessException extends InvalidDataAccessResourceUsageException {

    @Nullable
    private final String sql;


    /**
     * Constructor for InvalidResultSetAccessException.
     * @param task name of current task
     * @param sql the offending SQL statement
     * @param ex the root cause
     */
    public InvalidResultSetAccessException(String task, String sql, SQLException ex) {
        super(task + "; invalid ResultSet access for SQL [" + sql + "]", ex);
        this.sql = sql;
    }

    /**
     * Constructor for InvalidResultSetAccessException.
     * @param ex the root cause
     */
    public InvalidResultSetAccessException(SQLException ex) {
        super(ex.getMessage(), ex);
        this.sql = null;
    }


    /**
     * Return the wrapped SQLException.
     */
    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    /**
     * Return the SQL that caused the problem.
     * @return the offending SQL, if known
     */
    @Nullable
    public String getSql() {
        return this.sql;
    }

}
