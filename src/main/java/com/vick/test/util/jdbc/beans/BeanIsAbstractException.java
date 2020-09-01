package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
@SuppressWarnings("serial")
public class BeanIsAbstractException extends BeanCreationException {

    /**
     * Create a new BeanIsAbstractException.
     * @param beanName the name of the beans requested
     */
    public BeanIsAbstractException(String beanName) {
        super(beanName, "Bean definition is abstract");
    }

}
