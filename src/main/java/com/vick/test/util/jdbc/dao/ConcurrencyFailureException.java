package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class ConcurrencyFailureException extends TransientDataAccessException {
    public ConcurrencyFailureException(String msg) {
        super(msg);
    }

    public ConcurrencyFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
