package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class InvalidDataAccessResourceUsageException extends NonTransientDataAccessException {
    public InvalidDataAccessResourceUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessResourceUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}