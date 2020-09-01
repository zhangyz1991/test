package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface NamedBean {

    /**
     * Return the name of this beans in a Spring beans factory, if known.
     */
    String getBeanName();

}
