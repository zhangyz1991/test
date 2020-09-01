package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class CannotSerializeTransactionException extends PessimisticLockingFailureException {

    /**
     * Constructor for CannotSerializeTransactionException.
     * @param msg the detail message
     */
    public CannotSerializeTransactionException(String msg) {
        super(msg);
    }

    /**
     * Constructor for CannotSerializeTransactionException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public CannotSerializeTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
