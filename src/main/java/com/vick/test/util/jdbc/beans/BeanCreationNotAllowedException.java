package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class BeanCreationNotAllowedException extends BeanCreationException {

    /**
     * Create a new BeanCreationNotAllowedException.
     * @param beanName the name of the beans requested
     * @param msg the detail message
     */
    public BeanCreationNotAllowedException(String beanName, String msg) {
        super(beanName, msg);
    }

}
