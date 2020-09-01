package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class BeanCurrentlyInCreationException extends BeanCreationException {

    /**
     * Create a new BeanCurrentlyInCreationException,
     * with a default error message that indicates a circular reference.
     * @param beanName the name of the beans requested
     */
    public BeanCurrentlyInCreationException(String beanName) {
        super(beanName,
                "Requested beans is currently in creation: Is there an unresolvable circular reference?");
    }

    /**
     * Create a new BeanCurrentlyInCreationException.
     * @param beanName the name of the beans requested
     * @param msg the detail message
     */
    public BeanCurrentlyInCreationException(String beanName, String msg) {
        super(beanName, msg);
    }

}
