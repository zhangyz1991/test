package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class RecoverableDataAccessException extends DataAccessException {
    public RecoverableDataAccessException(String msg) {
        super(msg);
    }

    public RecoverableDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
