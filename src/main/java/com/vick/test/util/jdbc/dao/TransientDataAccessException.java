package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public abstract class TransientDataAccessException extends DataAccessException {
    public TransientDataAccessException(String msg) {
        super(msg);
    }

    public TransientDataAccessException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
