package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public class NullValueInNestedPathException extends InvalidPropertyException {

    /**
     * Create a new NullValueInNestedPathException.
     * @param beanClass the offending bean class
     * @param propertyName the offending property
     */
    public NullValueInNestedPathException(Class<?> beanClass, String propertyName) {
        super(beanClass, propertyName, "Value of nested property '" + propertyName + "' is null");
    }

    /**
     * Create a new NullValueInNestedPathException.
     * @param beanClass the offending bean class
     * @param propertyName the offending property
     * @param msg the detail message
     */
    public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg) {
        super(beanClass, propertyName, msg);
    }

    /**
     * Create a new NullValueInNestedPathException.
     * @param beanClass the offending bean class
     * @param propertyName the offending property
     * @param msg the detail message
     * @param cause the root cause
     * @since 4.3.2
     */
    public NullValueInNestedPathException(Class<?> beanClass, String propertyName, String msg, Throwable cause) {
        super(beanClass, propertyName, msg, cause);
    }

}
