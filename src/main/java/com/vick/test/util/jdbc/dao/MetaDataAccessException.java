package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class MetaDataAccessException extends NestedCheckedException {
    public MetaDataAccessException(String msg) {
        super(msg);
    }

    public MetaDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
