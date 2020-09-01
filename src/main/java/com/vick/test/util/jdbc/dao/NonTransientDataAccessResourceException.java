package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/23
 */
public class NonTransientDataAccessResourceException extends NonTransientDataAccessException {
    public NonTransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessResourceException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
