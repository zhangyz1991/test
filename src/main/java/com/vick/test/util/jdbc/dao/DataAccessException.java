package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.framework.NestedRuntimeException;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public abstract class DataAccessException extends NestedRuntimeException {
    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
