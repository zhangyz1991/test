package com.vick.test.util.jdbc.beans;

import java.beans.PropertyDescriptor;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface BeanWrapper extends ConfigurablePropertyAccessor {

    /**
     * Specify a limit for array and collection auto-growing.
     * <p>Default is unlimited on a plain BeanWrapper.
     * @since 4.1
     */
    void setAutoGrowCollectionLimit(int autoGrowCollectionLimit);

    /**
     * Return the limit for array and collection auto-growing.
     * @since 4.1
     */
    int getAutoGrowCollectionLimit();

    /**
     * Return the beans instance wrapped by this object.
     */
    Object getWrappedInstance();

    /**
     * Return the type of the wrapped beans instance.
     */
    Class<?> getWrappedClass();

    /**
     * Obtain the PropertyDescriptors for the wrapped object
     * (as determined by standard JavaBeans introspection).
     * @return the PropertyDescriptors for the wrapped object
     */
    PropertyDescriptor[] getPropertyDescriptors();

    /**
     * Obtain the property descriptor for a specific property
     * of the wrapped object.
     * @param propertyName the property to obtain the descriptor for
     * (may be a nested path, but no indexed/mapped property)
     * @return the property descriptor for the specified property
     * @throws InvalidPropertyException if there is no such property
     */
    PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException;

}
