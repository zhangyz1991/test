package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface BeanInfoFactory {

    /**
     * Return the beans info for the given class, if supported.
     * @param beanClass the beans class
     * @return the BeanInfo, or {@code null} if the given class is not supported
     * @throws IntrospectionException in case of exceptions
     */
    @Nullable
    BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException;

}
