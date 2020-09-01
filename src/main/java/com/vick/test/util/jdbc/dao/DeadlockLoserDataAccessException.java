package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class DeadlockLoserDataAccessException extends PessimisticLockingFailureException {

    /**
     * Constructor for DeadlockLoserDataAccessException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public DeadlockLoserDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
