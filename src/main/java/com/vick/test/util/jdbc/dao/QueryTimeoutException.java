package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class QueryTimeoutException extends TransientDataAccessException {
    public QueryTimeoutException(String msg) {
        super(msg);
    }

    public QueryTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
