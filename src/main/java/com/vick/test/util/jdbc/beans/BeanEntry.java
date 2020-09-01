package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class BeanEntry implements ParseState.Entry {

    private String beanDefinitionName;


    /**
     * Creates a new instance of {@link BeanEntry} class.
     * @param beanDefinitionName the name of the associated bean definition
     */
    public BeanEntry(String beanDefinitionName) {
        this.beanDefinitionName = beanDefinitionName;
    }


    @Override
    public String toString() {
        return "Bean '" + this.beanDefinitionName + "'";
    }

}
