package com.vick.test.util.jdbc.transaction;

import com.vick.test.util.jdbc.framework.NestedRuntimeException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public abstract class TransactionException extends NestedRuntimeException {
    public TransactionException(String msg) {
        super(msg);
    }

    public TransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
