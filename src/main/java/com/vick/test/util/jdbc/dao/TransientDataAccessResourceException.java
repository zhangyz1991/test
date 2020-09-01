package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class TransientDataAccessResourceException extends TransientDataAccessException {
    public TransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public TransientDataAccessResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
