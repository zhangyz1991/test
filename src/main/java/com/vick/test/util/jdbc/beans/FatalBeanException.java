package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class FatalBeanException extends BeansException {

    /**
     * Create a new FatalBeanException with the specified message.
     * @param msg the detail message
     */
    public FatalBeanException(String msg) {
        super(msg);
    }

    /**
     * Create a new FatalBeanException with the specified message
     * and root cause.
     * @param msg the detail message
     * @param cause the root cause
     */
    public FatalBeanException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}
