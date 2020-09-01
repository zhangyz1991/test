package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

public interface HierarchicalBeanFactory extends BeanFactory {

    /**
     * Return the parent beans factory, or {@code null} if there is none.
     */
    @Nullable
    BeanFactory getParentBeanFactory();

    /**
     * Return whether the local beans factory contains a beans of the given name,
     * ignoring beans defined in ancestor contexts.
     * <p>This is an alternative to {@code containsBean}, ignoring a beans
     * of the given name from an ancestor beans factory.
     * @param name the name of the beans to query
     * @return whether a beans with the given name is defined in the local factory
     * @see BeanFactory#containsBean
     */
    boolean containsLocalBean(String name);

}
