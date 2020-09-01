package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public final class PropertyAccessorFactory {

    private PropertyAccessorFactory() {
    }


    /**
     * Obtain a BeanWrapper for the given target object,
     * accessing properties in JavaBeans style.
     * @param target the target object to wrap
     * @return the property accessor
     * @see BeanWrapperImpl
     */
    public static BeanWrapper forBeanPropertyAccess(Object target) {
        return new BeanWrapperImpl(target);
    }

    /**
     * Obtain a PropertyAccessor for the given target object,
     * accessing properties in direct field style.
     * @param target the target object to wrap
     * @return the property accessor
     * @see DirectFieldAccessor
     */
    public static ConfigurablePropertyAccessor forDirectFieldAccess(Object target) {
        return new DirectFieldAccessor(target);
    }

}
