package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class BeanInstantiationException extends FatalBeanException {

    private final Class<?> beanClass;

    @Nullable
    private final Constructor<?> constructor;

    @Nullable
    private final Method constructingMethod;


    /**
     * Create a new BeanInstantiationException.
     * @param beanClass the offending beans class
     * @param msg the detail message
     */
    public BeanInstantiationException(Class<?> beanClass, String msg) {
        this(beanClass, msg, null);
    }

    /**
     * Create a new BeanInstantiationException.
     * @param beanClass the offending beans class
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeanInstantiationException(Class<?> beanClass, String msg, @Nullable Throwable cause) {
        super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
        this.constructor = null;
        this.constructingMethod = null;
    }

    /**
     * Create a new BeanInstantiationException.
     * @param constructor the offending constructor
     * @param msg the detail message
     * @param cause the root cause
     * @since 4.3
     */
    public BeanInstantiationException(Constructor<?> constructor, String msg, @Nullable Throwable cause) {
        super("Failed to instantiate [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
        this.beanClass = constructor.getDeclaringClass();
        this.constructor = constructor;
        this.constructingMethod = null;
    }

    /**
     * Create a new BeanInstantiationException.
     * @param constructingMethod the delegate for beans construction purposes
     * (typically, but not necessarily, a static factory method)
     * @param msg the detail message
     * @param cause the root cause
     * @since 4.3
     */
    public BeanInstantiationException(Method constructingMethod, String msg, @Nullable Throwable cause) {
        super("Failed to instantiate [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
        this.beanClass = constructingMethod.getReturnType();
        this.constructor = null;
        this.constructingMethod = constructingMethod;
    }


    /**
     * Return the offending beans class (never {@code null}).
     * @return the class that was to be instantiated
     */
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    /**
     * Return the offending constructor, if known.
     * @return the constructor in use, or {@code null} in case of a
     * factory method or in case of default instantiation
     * @since 4.3
     */
    @Nullable
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    /**
     * Return the delegate for beans construction purposes, if known.
     * @return the method in use (typically a static factory method),
     * or {@code null} in case of constructor-based instantiation
     * @since 4.3
     */
    @Nullable
    public Method getConstructingMethod() {
        return this.constructingMethod;
    }

}
