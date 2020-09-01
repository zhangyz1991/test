package com.vick.test.util.jdbc.dao;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class DuplicateKeyException extends DataIntegrityViolationException {

    /**
     * Constructor for DuplicateKeyException.
     * @param msg the detail message
     */
    public DuplicateKeyException(String msg) {
        super(msg);
    }

    /**
     * Constructor for DuplicateKeyException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public DuplicateKeyException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
