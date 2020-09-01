package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class BeanComponentDefinition extends BeanDefinitionHolder implements ComponentDefinition {

    private BeanDefinition[] innerBeanDefinitions;

    private BeanReference[] beanReferences;


    /**
     * Create a new BeanComponentDefinition for the given bean.
     * @param beanDefinition the BeanDefinition
     * @param beanName the name of the bean
     */
    public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName) {
        this(new BeanDefinitionHolder(beanDefinition, beanName));
    }

    /**
     * Create a new BeanComponentDefinition for the given bean.
     * @param beanDefinition the BeanDefinition
     * @param beanName the name of the bean
     * @param aliases alias names for the bean, or {@code null} if none
     */
    public BeanComponentDefinition(BeanDefinition beanDefinition, String beanName, @Nullable String[] aliases) {
        this(new BeanDefinitionHolder(beanDefinition, beanName, aliases));
    }

    /**
     * Create a new BeanComponentDefinition for the given bean.
     * @param beanDefinitionHolder the BeanDefinitionHolder encapsulating
     * the bean definition as well as the name of the bean
     */
    public BeanComponentDefinition(BeanDefinitionHolder beanDefinitionHolder) {
        super(beanDefinitionHolder);

        List<BeanDefinition> innerBeans = new ArrayList<>();
        List<BeanReference> references = new ArrayList<>();
        PropertyValues propertyValues = beanDefinitionHolder.getBeanDefinition().getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanDefinitionHolder) {
                innerBeans.add(((BeanDefinitionHolder) value).getBeanDefinition());
            }
            else if (value instanceof BeanDefinition) {
                innerBeans.add((BeanDefinition) value);
            }
            else if (value instanceof BeanReference) {
                references.add((BeanReference) value);
            }
        }
        this.innerBeanDefinitions = innerBeans.toArray(new BeanDefinition[0]);
        this.beanReferences = references.toArray(new BeanReference[0]);
    }


    @Override
    public String getName() {
        return getBeanName();
    }

    @Override
    public String getDescription() {
        return getShortDescription();
    }

    @Override
    public BeanDefinition[] getBeanDefinitions() {
        return new BeanDefinition[] {getBeanDefinition()};
    }

    @Override
    public BeanDefinition[] getInnerBeanDefinitions() {
        return this.innerBeanDefinitions;
    }

    @Override
    public BeanReference[] getBeanReferences() {
        return this.beanReferences;
    }


    /**
     * This implementation returns this ComponentDefinition's description.
     * @see #getDescription()
     */
    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * This implementations expects the other object to be of type BeanComponentDefinition
     * as well, in addition to the superclass's equality requirements.
     */
    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof BeanComponentDefinition && super.equals(other)));
    }

}
