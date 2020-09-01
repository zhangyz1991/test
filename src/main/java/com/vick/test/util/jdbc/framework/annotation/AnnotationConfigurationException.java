package com.vick.test.util.jdbc.framework.annotation;

import com.vick.test.util.jdbc.framework.NestedRuntimeException;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class AnnotationConfigurationException extends NestedRuntimeException {

    /**
     * Construct a new {@code AnnotationConfigurationException} with the
     * supplied message.
     * @param message the detail message
     */
    public AnnotationConfigurationException(String message) {
        super(message);
    }

    /**
     * Construct a new {@code AnnotationConfigurationException} with the
     * supplied message and cause.
     * @param message the detail message
     * @param cause the root cause
     */
    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
