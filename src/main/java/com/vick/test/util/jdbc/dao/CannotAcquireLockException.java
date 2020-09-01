package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class CannotAcquireLockException extends PessimisticLockingFailureException {

    /**
     * Constructor for CannotAcquireLockException.
     * @param msg the detail message
     */
    public CannotAcquireLockException(String msg) {
        super(msg);
    }

    /**
     * Constructor for CannotAcquireLockException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public CannotAcquireLockException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
