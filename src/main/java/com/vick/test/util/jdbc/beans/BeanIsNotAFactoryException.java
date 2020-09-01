package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
@SuppressWarnings("serial")
public class BeanIsNotAFactoryException extends BeanNotOfRequiredTypeException {

    /**
     * Create a new BeanIsNotAFactoryException.
     * @param name the name of the beans requested
     * @param actualType the actual type returned, which did not match
     * the expected type
     */
    public BeanIsNotAFactoryException(String name, Class<?> actualType) {
        super(name, FactoryBean.class, actualType);
    }

}
