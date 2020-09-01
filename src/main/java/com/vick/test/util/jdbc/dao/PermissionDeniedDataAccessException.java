package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class PermissionDeniedDataAccessException extends NonTransientDataAccessException {
    public PermissionDeniedDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
