package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
@SuppressWarnings("serial")
public class CannotLoadBeanClassException extends FatalBeanException {

    @Nullable
    private final String resourceDescription;

    private final String beanName;

    @Nullable
    private final String beanClassName;


    /**
     * Create a new CannotLoadBeanClassException.
     * @param resourceDescription description of the resource
     * that the beans definition came from
     * @param beanName the name of the beans requested
     * @param beanClassName the name of the beans class
     * @param cause the root cause
     */
    public CannotLoadBeanClassException(@Nullable String resourceDescription, String beanName,
                                        @Nullable String beanClassName, ClassNotFoundException cause) {

        super("Cannot find class [" + beanClassName + "] for beans with name '" + beanName + "'" +
                (resourceDescription != null ? " defined in " + resourceDescription : ""), cause);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
        this.beanClassName = beanClassName;
    }

    /**
     * Create a new CannotLoadBeanClassException.
     * @param resourceDescription description of the resource
     * that the beans definition came from
     * @param beanName the name of the beans requested
     * @param beanClassName the name of the beans class
     * @param cause the root cause
     */
    public CannotLoadBeanClassException(@Nullable String resourceDescription, String beanName,
                                        @Nullable String beanClassName, LinkageError cause) {

        super("Error loading class [" + beanClassName + "] for beans with name '" + beanName + "'" +
                (resourceDescription != null ? " defined in " + resourceDescription : "") +
                ": problem with class file or dependent class", cause);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
        this.beanClassName = beanClassName;
    }


    /**
     * Return the description of the resource that the beans
     * definition came from.
     */
    @Nullable
    public String getResourceDescription() {
        return this.resourceDescription;
    }

    /**
     * Return the name of the beans requested.
     */
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the name of the class we were trying to load.
     */
    @Nullable
    public String getBeanClassName() {
        return this.beanClassName;
    }

}
