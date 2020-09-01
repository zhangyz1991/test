package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.NestedRuntimeException;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public abstract class BeansException extends NestedRuntimeException {

    /**
     * Create a new BeansException with the specified message.
     * @param msg the detail message
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeansException with the specified message
     * and root cause.
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

}
