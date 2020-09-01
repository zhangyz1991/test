package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class PessimisticLockingFailureException extends ConcurrencyFailureException {

    /**
     * Constructor for PessimisticLockingFailureException.
     * @param msg the detail message
     */
    public PessimisticLockingFailureException(String msg) {
        super(msg);
    }

    /**
     * Constructor for PessimisticLockingFailureException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public PessimisticLockingFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
