package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class InvalidDataAccessApiUsageException extends NonTransientDataAccessException {
    public InvalidDataAccessApiUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessApiUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
