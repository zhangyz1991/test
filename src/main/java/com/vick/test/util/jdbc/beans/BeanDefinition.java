package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.AttributeAccessor;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResolvableType;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    /**
     * Scope identifier for the standard singleton scope: {@value}.
     * <p>Note that extended beans factories might support further scopes.
     * @see #setScope
     * @see ConfigurableBeanFactory#SCOPE_SINGLETON
     */
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    /**
     * Scope identifier for the standard prototype scope: {@value}.
     * <p>Note that extended beans factories might support further scopes.
     * @see #setScope
     * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
     */
    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


    /**
     * Role hint indicating that a {@code BeanDefinition} is a major part
     * of the application. Typically corresponds to a user-defined beans.
     */
    int ROLE_APPLICATION = 0;

    /**
     * Role hint indicating that a {@code BeanDefinition} is a supporting
     * part of some larger configuration, typically an outer
     * {link org.springframework.beans.factory.parsing.ComponentDefinition}.
     * {@code SUPPORT} beans are considered important enough to be aware
     * of when looking more closely at a particular
     * {link org.springframework.beans.factory.parsing.ComponentDefinition},
     * but not when looking at the overall configuration of an application.
     */
    int ROLE_SUPPORT = 1;

    /**
     * Role hint indicating that a {@code BeanDefinition} is providing an
     * entirely background role and has no relevance to the end-user. This hint is
     * used when registering beans that are completely part of the internal workings
     * of a {link org.springframework.beans.factory.parsing.ComponentDefinition}.
     */
    int ROLE_INFRASTRUCTURE = 2;


    // Modifiable attributes

    /**
     * Set the name of the parent definition of this beans definition, if any.
     */
    void setParentName(@Nullable String parentName);

    /**
     * Return the name of the parent definition of this beans definition, if any.
     */
    @Nullable
    String getParentName();

    /**
     * Specify the beans class name of this beans definition.
     * <p>The class name can be modified during beans factory post-processing,
     * typically replacing the original class name with a parsed variant of it.
     * @see #setParentName
     * @see #setFactoryBeanName
     * @see #setFactoryMethodName
     */
    void setBeanClassName(@Nullable String beanClassName);

    /**
     * Return the current beans class name of this beans definition.
     * <p>Note that this does not have to be the actual class name used at runtime, in
     * case of a child definition overriding/inheriting the class name from its parent.
     * Also, this may just be the class that a factory method is called on, or it may
     * even be empty in case of a factory beans reference that a method is called on.
     * Hence, do <i>not</i> consider this to be the definitive beans type at runtime but
     * rather only use it for parsing purposes at the individual beans definition level.
     * @see #getParentName()
     * @see #getFactoryBeanName()
     * @see #getFactoryMethodName()
     */
    @Nullable
    String getBeanClassName();

    /**
     * Override the target scope of this beans, specifying a new scope name.
     * @see #SCOPE_SINGLETON
     * @see #SCOPE_PROTOTYPE
     */
    void setScope(@Nullable String scope);

    /**
     * Return the name of the current target scope for this beans,
     * or {@code null} if not known yet.
     */
    @Nullable
    String getScope();

    /**
     * Set whether this beans should be lazily initialized.
     * <p>If {@code false}, the beans will get instantiated on startup by beans
     * factories that perform eager initialization of singletons.
     */
    void setLazyInit(boolean lazyInit);

    /**
     * Return whether this beans should be lazily initialized, i.e. not
     * eagerly instantiated on startup. Only applicable to a singleton beans.
     */
    boolean isLazyInit();

    /**
     * Set the names of the beans that this beans depends on being initialized.
     * The beans factory will guarantee that these beans get initialized first.
     */
    void setDependsOn(@Nullable String... dependsOn);

    /**
     * Return the beans names that this beans depends on.
     */
    @Nullable
    String[] getDependsOn();

    /**
     * Set whether this beans is a candidate for getting autowired into some other beans.
     * <p>Note that this flag is designed to only affect type-based autowiring.
     * It does not affect explicit references by name, which will get resolved even
     * if the specified beans is not marked as an autowire candidate. As a consequence,
     * autowiring by name will nevertheless inject a beans if the name matches.
     */
    void setAutowireCandidate(boolean autowireCandidate);

    /**
     * Return whether this beans is a candidate for getting autowired into some other beans.
     */
    boolean isAutowireCandidate();

    /**
     * Set whether this beans is a primary autowire candidate.
     * <p>If this value is {@code true} for exactly one beans among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    void setPrimary(boolean primary);

    /**
     * Return whether this beans is a primary autowire candidate.
     */
    boolean isPrimary();

    /**
     * Specify the factory beans to use, if any.
     * This the name of the beans to call the specified factory method on.
     * @see #setFactoryMethodName
     */
    void setFactoryBeanName(@Nullable String factoryBeanName);

    /**
     * Return the factory beans name, if any.
     */
    @Nullable
    String getFactoryBeanName();

    /**
     * Specify a factory method, if any. This method will be invoked with
     * constructor arguments, or with no arguments if none are specified.
     * The method will be invoked on the specified factory beans, if any,
     * or otherwise as a static method on the local beans class.
     * @see #setFactoryBeanName
     * @see #setBeanClassName
     */
    void setFactoryMethodName(@Nullable String factoryMethodName);

    /**
     * Return a factory method, if any.
     */
    @Nullable
    String getFactoryMethodName();

    /**
     * Return the constructor argument values for this beans.
     * <p>The returned instance can be modified during beans factory post-processing.
     * @return the ConstructorArgumentValues object (never {@code null})
     */
    ConstructorArgumentValues getConstructorArgumentValues();

    /**
     * Return if there are constructor argument values defined for this beans.
     * @since 5.0.2
     */
    default boolean hasConstructorArgumentValues() {
        return !getConstructorArgumentValues().isEmpty();
    }

    /**
     * Return the property values to be applied to a new instance of the beans.
     * <p>The returned instance can be modified during beans factory post-processing.
     * @return the MutablePropertyValues object (never {@code null})
     */
    MutablePropertyValues getPropertyValues();

    /**
     * Return if there are property values values defined for this beans.
     * @since 5.0.2
     */
    default boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }

    /**
     * Set the name of the initializer method.
     * @since 5.1
     */
    void setInitMethodName(@Nullable String initMethodName);

    /**
     * Return the name of the initializer method.
     * @since 5.1
     */
    @Nullable
    String getInitMethodName();

    /**
     * Set the name of the destroy method.
     * @since 5.1
     */
    void setDestroyMethodName(@Nullable String destroyMethodName);

    /**
     * Return the name of the destroy method.
     * @since 5.1
     */
    @Nullable
    String getDestroyMethodName();

    /**
     * Set the role hint for this {@code BeanDefinition}. The role hint
     * provides the frameworks as well as tools with an indication of
     * the role and importance of a particular {@code BeanDefinition}.
     * @since 5.1
     * @see #ROLE_APPLICATION
     * @see #ROLE_SUPPORT
     * @see #ROLE_INFRASTRUCTURE
     */
    void setRole(int role);

    /**
     * Get the role hint for this {@code BeanDefinition}. The role hint
     * provides the frameworks as well as tools with an indication of
     * the role and importance of a particular {@code BeanDefinition}.
     * @see #ROLE_APPLICATION
     * @see #ROLE_SUPPORT
     * @see #ROLE_INFRASTRUCTURE
     */
    int getRole();

    /**
     * Set a human-readable description of this beans definition.
     * @since 5.1
     */
    void setDescription(@Nullable String description);

    /**
     * Return a human-readable description of this beans definition.
     */
    @Nullable
    String getDescription();


    // Read-only attributes

    /**
     * Return a resolvable type for this beans definition,
     * based on the beans class or other specific metadata.
     * <p>This is typically fully resolved on a runtime-merged beans definition
     * but not necessarily on a configuration-time definition instance.
     * @return the resolvable type (potentially {@link ResolvableType#NONE})
     * @since 5.2
     * @see ConfigurableBeanFactory#getMergedBeanDefinition
     */
    ResolvableType getResolvableType();

    /**
     * Return whether this a <b>Singleton</b>, with a single, shared instance
     * returned on all calls.
     * @see #SCOPE_SINGLETON
     */
    boolean isSingleton();

    /**
     * Return whether this a <b>Prototype</b>, with an independent instance
     * returned for each call.
     * @since 3.0
     * @see #SCOPE_PROTOTYPE
     */
    boolean isPrototype();

    /**
     * Return whether this beans is "abstract", that is, not meant to be instantiated.
     */
    boolean isAbstract();

    /**
     * Return a description of the resource that this beans definition
     * came from (for the purpose of showing context in case of errors).
     */
    @Nullable
    String getResourceDescription();

    /**
     * Return the originating BeanDefinition, or {@code null} if none.
     * Allows for retrieving the decorated beans definition, if any.
     * <p>Note that this method returns the immediate originator. Iterate through the
     * originator chain to find the original BeanDefinition as defined by the user.
     */
    @Nullable
    BeanDefinition getOriginatingBeanDefinition();

}
