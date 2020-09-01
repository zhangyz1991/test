package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ObjectUtils;
import com.vick.test.util.jdbc.framework.StringUtils;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class BeanDefinitionHolder implements BeanMetadataElement {

    private final BeanDefinition beanDefinition;

    private final String beanName;

    @Nullable
    private final String[] aliases;


    /**
     * Create a new BeanDefinitionHolder.
     * @param beanDefinition the BeanDefinition to wrap
     * @param beanName the name of the beans, as specified for the beans definition
     */
    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
        this(beanDefinition, beanName, null);
    }

    /**
     * Create a new BeanDefinitionHolder.
     * @param beanDefinition the BeanDefinition to wrap
     * @param beanName the name of the beans, as specified for the beans definition
     * @param aliases alias names for the beans, or {@code null} if none
     */
    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, @Nullable String[] aliases) {
        Assert.notNull(beanDefinition, "BeanDefinition must not be null");
        Assert.notNull(beanName, "Bean name must not be null");
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliases = aliases;
    }

    /**
     * Copy constructor: Create a new BeanDefinitionHolder with the
     * same contents as the given BeanDefinitionHolder instance.
     * <p>Note: The wrapped BeanDefinition reference is taken as-is;
     * it is {@code not} deeply copied.
     * @param beanDefinitionHolder the BeanDefinitionHolder to copy
     */
    public BeanDefinitionHolder(BeanDefinitionHolder beanDefinitionHolder) {
        Assert.notNull(beanDefinitionHolder, "BeanDefinitionHolder must not be null");
        this.beanDefinition = beanDefinitionHolder.getBeanDefinition();
        this.beanName = beanDefinitionHolder.getBeanName();
        this.aliases = beanDefinitionHolder.getAliases();
    }


    /**
     * Return the wrapped BeanDefinition.
     */
    public BeanDefinition getBeanDefinition() {
        return this.beanDefinition;
    }

    /**
     * Return the primary name of the beans, as specified for the beans definition.
     */
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the alias names for the beans, as specified directly for the beans definition.
     * @return the array of alias names, or {@code null} if none
     */
    @Nullable
    public String[] getAliases() {
        return this.aliases;
    }

    /**
     * Expose the beans definition's source object.
     * @see BeanDefinition#getSource()
     */
    @Override
    @Nullable
    public Object getSource() {
        return this.beanDefinition.getSource();
    }

    /**
     * Determine whether the given candidate name matches the beans name
     * or the aliases stored in this beans definition.
     */
    public boolean matchesName(@Nullable String candidateName) {
        return (candidateName != null && (candidateName.equals(this.beanName) ||
                candidateName.equals(BeanFactoryUtils.transformedBeanName(this.beanName)) ||
                ObjectUtils.containsElement(this.aliases, candidateName)));
    }


    /**
     * Return a friendly, short description for the beans, stating name and aliases.
     * @see #getBeanName()
     * @see #getAliases()
     */
    public String getShortDescription() {
        if (this.aliases == null) {
            return "Bean definition with name '" + this.beanName + "'";
        }
        return "Bean definition with name '" + this.beanName + "' and aliases [" + StringUtils.arrayToCommaDelimitedString(this.aliases) + ']';
    }

    /**
     * Return a long description for the beans, including name and aliases
     * as well as a description of the contained {@link BeanDefinition}.
     * @see #getShortDescription()
     * @see #getBeanDefinition()
     */
    public String getLongDescription() {
        return getShortDescription() + ": " + this.beanDefinition;
    }

    /**
     * This implementation returns the long description. Can be overridden
     * to return the short description or any kind of custom description instead.
     * @see #getLongDescription()
     * @see #getShortDescription()
     */
    @Override
    public String toString() {
        return getLongDescription();
    }


    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeanDefinitionHolder)) {
            return false;
        }
        BeanDefinitionHolder otherHolder = (BeanDefinitionHolder) other;
        return this.beanDefinition.equals(otherHolder.beanDefinition) &&
                this.beanName.equals(otherHolder.beanName) &&
                ObjectUtils.nullSafeEquals(this.aliases, otherHolder.aliases);
    }

    @Override
    public int hashCode() {
        int hashCode = this.beanDefinition.hashCode();
        hashCode = 29 * hashCode + this.beanName.hashCode();
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.aliases);
        return hashCode;
    }

}
