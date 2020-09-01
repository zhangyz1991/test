package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class NamedBeanHolder<T> implements NamedBean {

    private final String beanName;

    private final T beanInstance;


    /**
     * Create a new holder for the given beans name plus instance.
     * @param beanName the name of the beans
     * @param beanInstance the corresponding beans instance
     */
    public NamedBeanHolder(String beanName, T beanInstance) {
        Assert.notNull(beanName, "Bean name must not be null");
        this.beanName = beanName;
        this.beanInstance = beanInstance;
    }


    /**
     * Return the name of the beans.
     */
    @Override
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the corresponding beans instance.
     */
    public T getBeanInstance() {
        return this.beanInstance;
    }

}
