package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
@SuppressWarnings("serial")
public class InvalidPropertyException extends FatalBeanException {

    private final Class<?> beanClass;

    private final String propertyName;


    /**
     * Create a new InvalidPropertyException.
     * @param beanClass the offending beans class
     * @param propertyName the offending property
     * @param msg the detail message
     */
    public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg) {
        this(beanClass, propertyName, msg, null);
    }

    /**
     * Create a new InvalidPropertyException.
     * @param beanClass the offending beans class
     * @param propertyName the offending property
     * @param msg the detail message
     * @param cause the root cause
     */
    public InvalidPropertyException(Class<?> beanClass, String propertyName, String msg, @Nullable Throwable cause) {
        super("Invalid property '" + propertyName + "' of beans class [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
        this.propertyName = propertyName;
    }

    /**
     * Return the offending beans class.
     */
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    /**
     * Return the name of the offending property.
     */
    public String getPropertyName() {
        return this.propertyName;
    }

}
