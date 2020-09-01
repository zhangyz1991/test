package com.vick.test.util.jdbc;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.dao.NonTransientDataAccessException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class DataRetrievalFailureException extends NonTransientDataAccessException {

    /**
     * Constructor for DataRetrievalFailureException.
     * @param msg the detail message
     */
    public DataRetrievalFailureException(String msg) {
        super(msg);
    }

    /**
     * Constructor for DataRetrievalFailureException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public DataRetrievalFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}
