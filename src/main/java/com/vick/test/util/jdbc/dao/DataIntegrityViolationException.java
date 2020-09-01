package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class DataIntegrityViolationException extends NonTransientDataAccessException {
    public DataIntegrityViolationException(String msg) {
        super(msg);
    }

    public DataIntegrityViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
