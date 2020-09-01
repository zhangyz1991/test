package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public abstract class NonTransientDataAccessException extends DataAccessException {
    public NonTransientDataAccessException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
