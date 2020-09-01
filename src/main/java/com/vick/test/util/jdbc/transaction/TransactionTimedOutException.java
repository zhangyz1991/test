package com.vick.test.util.jdbc.transaction;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class TransactionTimedOutException extends TransactionException {

    /**
     * Constructor for TransactionTimedOutException.
     * @param msg the detail message
     */
    public TransactionTimedOutException(String msg) {
        super(msg);
    }

    /**
     * Constructor for TransactionTimedOutException.
     * @param msg the detail message
     * @param cause the root cause from the transaction API in use
     */
    public TransactionTimedOutException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
