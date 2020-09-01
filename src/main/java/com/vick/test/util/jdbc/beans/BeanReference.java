package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface BeanReference extends BeanMetadataElement {

    /**
     * Return the target bean name that this reference points to (never {@code null}).
     */
    String getBeanName();

}
