package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResolvableType;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public class NoSuchBeanDefinitionException extends BeansException {

    @Nullable
    private final String beanName;

    @Nullable
    private final ResolvableType resolvableType;


    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param name the name of the missing bean
     */
    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
        this.resolvableType = null;
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param name the name of the missing bean
     * @param message detailed message describing the problem
     */
    public NoSuchBeanDefinitionException(String name, String message) {
        super("No bean named '" + name + "' available: " + message);
        this.beanName = name;
        this.resolvableType = null;
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param type required type of the missing bean
     */
    public NoSuchBeanDefinitionException(Class<?> type) {
        this(ResolvableType.forClass(type));
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param type required type of the missing bean
     * @param message detailed message describing the problem
     */
    public NoSuchBeanDefinitionException(Class<?> type, String message) {
        this(ResolvableType.forClass(type), message);
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param type full type declaration of the missing bean
     * @since 4.3.4
     */
    public NoSuchBeanDefinitionException(ResolvableType type) {
        super("No qualifying bean of type '" + type + "' available");
        this.beanName = null;
        this.resolvableType = type;
    }

    /**
     * Create a new {@code NoSuchBeanDefinitionException}.
     * @param type full type declaration of the missing bean
     * @param message detailed message describing the problem
     * @since 4.3.4
     */
    public NoSuchBeanDefinitionException(ResolvableType type, String message) {
        super("No qualifying bean of type '" + type + "' available: " + message);
        this.beanName = null;
        this.resolvableType = type;
    }


    /**
     * Return the name of the missing bean, if it was a lookup <em>by name</em> that failed.
     */
    @Nullable
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the required type of the missing bean, if it was a lookup <em>by type</em>
     * that failed.
     */
    @Nullable
    public Class<?> getBeanType() {
        return (this.resolvableType != null ? this.resolvableType.resolve() : null);
    }

    /**
     * Return the required {@link ResolvableType} of the missing bean, if it was a lookup
     * <em>by type</em> that failed.
     * @since 4.3.4
     */
    @Nullable
    public ResolvableType getResolvableType() {
        return this.resolvableType;
    }

    /**
     * Return the number of beans found when only one matching bean was expected.
     * For a regular NoSuchBeanDefinitionException, this will always be 0.
     * see NoUniqueBeanDefinitionException
     */
    public int getNumberOfBeansFound() {
        return 0;
    }

}
