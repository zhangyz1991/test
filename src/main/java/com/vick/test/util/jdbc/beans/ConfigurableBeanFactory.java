package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.convert.ConversionService;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * Scope identifier for the standard singleton scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: {@value}.
     * <p>Custom scopes can be added via {@code registerScope}.
     * @see #registerScope
     */
    String SCOPE_PROTOTYPE = "prototype";


    /**
     * Set the parent of this beans factory.
     * <p>Note that the parent cannot be changed: It should only be set outside
     * a constructor if it isn't available at the time of factory instantiation.
     * @param parentBeanFactory the parent BeanFactory
     * @throws IllegalStateException if this factory is already associated with
     * a parent BeanFactory
     * @see #getParentBeanFactory()
     */
    void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

    /**
     * Set the class loader to use for loading beans classes.
     * Default is the thread context class loader.
     * <p>Note that this class loader will only apply to beans definitions
     * that do not carry a resolved beans class yet. This is the case as of
     * Spring 2.0 by default: Bean definitions only carry beans class names,
     * to be resolved once the factory processes the beans definition.
     * @param beanClassLoader the class loader to use,
     * or {@code null} to suggest the default class loader
     */
    void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

    /**
     * Return this factory's class loader for loading beans classes
     * (only {@code null} if even the system ClassLoader isn't accessible).
     * see org.springframework.util.ClassUtils#forName(String, ClassLoader)
     */
    @Nullable
    ClassLoader getBeanClassLoader();

    /**
     * Specify a temporary ClassLoader to use for type matching purposes.
     * Default is none, simply using the standard beans ClassLoader.
     * <p>A temporary ClassLoader is usually just specified if
     * <i>load-time weaving</i> is involved, to make sure that actual beans
     * classes are loaded as lazily as possible. The temporary loader is
     * then removed once the BeanFactory completes its bootstrap phase.
     * @since 2.5
     */
    void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

    /**
     * Return the temporary ClassLoader to use for type matching purposes,
     * if any.
     * @since 2.5
     */
    @Nullable
    ClassLoader getTempClassLoader();

    /**
     * Set whether to cache beans metadata such as given beans definitions
     * (in merged fashion) and resolved beans classes. Default is on.
     * <p>Turn this flag off to enable hot-refreshing of beans definition objects
     * and in particular beans classes. If this flag is off, any creation of a beans
     * instance will re-query the beans class loader for newly resolved classes.
     */
    void setCacheBeanMetadata(boolean cacheBeanMetadata);

    /**
     * Return whether to cache beans metadata such as given beans definitions
     * (in merged fashion) and resolved beans classes.
     */
    boolean isCacheBeanMetadata();

    /**
     * Specify the resolution strategy for expressions in beans definition values.
     * <p>There is no expression support active in a BeanFactory by default.
     * An ApplicationContext will typically set a standard expression strategy
     * here, supporting "#{...}" expressions in a Unified EL compatible style.
     * @since 3.0
     */
    void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

    /**
     * Return the resolution strategy for expressions in beans definition values.
     * @since 3.0
     */
    @Nullable
    BeanExpressionResolver getBeanExpressionResolver();

    /**
     * Specify a Spring 3.0 ConversionService to use for converting
     * property values, as an alternative to JavaBeans PropertyEditors.
     * @since 3.0
     */
    void setConversionService(@Nullable ConversionService conversionService);

    /**
     * Return the associated ConversionService, if any.
     * @since 3.0
     */
    @Nullable
    ConversionService getConversionService();

    /**
     * Add a PropertyEditorRegistrar to be applied to all beans creation processes.
     * <p>Such a registrar creates new PropertyEditor instances and registers them
     * on the given registry, fresh for each beans creation attempt. This avoids
     * the need for synchronization on custom editors; hence, it is generally
     * preferable to use this method instead of {@link #registerCustomEditor}.
     * @param registrar the PropertyEditorRegistrar to register
     */
    void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

    /**
     * Register the given custom property editor for all properties of the
     * given type. To be invoked during factory configuration.
     * <p>Note that this method will register a shared custom editor instance;
     * access to that instance will be synchronized for thread-safety. It is
     * generally preferable to use {@link #addPropertyEditorRegistrar} instead
     * of this method, to avoid for the need for synchronization on custom editors.
     * @param requiredType type of the property
     * @param propertyEditorClass the {@link PropertyEditor} class to register
     */
    void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

    /**
     * Initialize the given PropertyEditorRegistry with the custom editors
     * that have been registered with this BeanFactory.
     * @param registry the PropertyEditorRegistry to initialize
     */
    void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

    /**
     * Set a custom type converter that this BeanFactory should use for converting
     * beans property values, constructor argument values, etc.
     * <p>This will override the default PropertyEditor mechanism and hence make
     * any custom editors or custom editor registrars irrelevant.
     * @since 2.5
     * @see #addPropertyEditorRegistrar
     * @see #registerCustomEditor
     */
    void setTypeConverter(TypeConverter typeConverter);

    /**
     * Obtain a type converter as used by this BeanFactory. This may be a fresh
     * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
     * <p>If the default PropertyEditor mechanism is active, the returned
     * TypeConverter will be aware of all custom editors that have been registered.
     * @since 2.5
     */
    TypeConverter getTypeConverter();

    /**
     * Add a String resolver for embedded values such as annotation attributes.
     * @param valueResolver the String resolver to apply to embedded values
     * @since 3.0
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * Determine whether an embedded value resolver has been registered with this
     * beans factory, to be applied through {@link #resolveEmbeddedValue(String)}.
     * @since 4.3
     */
    boolean hasEmbeddedValueResolver();

    /**
     * Resolve the given embedded value, e.g. an annotation attribute.
     * @param value the value to resolve
     * @return the resolved value (may be the original value as-is)
     * @since 3.0
     */
    @Nullable
    String resolveEmbeddedValue(String value);

    /**
     * Add a new BeanPostProcessor that will get applied to beans created
     * by this factory. To be invoked during factory configuration.
     * <p>Note: Post-processors submitted here will be applied in the order of
     * registration; any ordering semantics expressed through implementing the
     * {link org.springframework.core.Ordered} interface will be ignored. Note
     * that autodetected post-processors (e.g. as beans in an ApplicationContext)
     * will always be applied after programmatically registered ones.
     * @param beanPostProcessor the post-processor to register
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * Return the current number of registered BeanPostProcessors, if any.
     */
    int getBeanPostProcessorCount();

    /**
     * Register the given scope, backed by the given Scope implementation.
     * @param scopeName the scope identifier
     * @param scope the backing Scope implementation
     */
    void registerScope(String scopeName, Scope scope);

    /**
     * Return the names of all currently registered scopes.
     * <p>This will only return the names of explicitly registered scopes.
     * Built-in scopes such as "singleton" and "prototype" won't be exposed.
     * @return the array of scope names, or an empty array if none
     * @see #registerScope
     */
    String[] getRegisteredScopeNames();

    /**
     * Return the Scope implementation for the given scope name, if any.
     * <p>This will only return explicitly registered scopes.
     * Built-in scopes such as "singleton" and "prototype" won't be exposed.
     * @param scopeName the name of the scope
     * @return the registered Scope implementation, or {@code null} if none
     * @see #registerScope
     */
    @Nullable
    Scope getRegisteredScope(String scopeName);

    /**
     * Provides a security access control context relevant to this factory.
     * @return the applicable AccessControlContext (never {@code null})
     * @since 3.0
     */
    AccessControlContext getAccessControlContext();

    /**
     * Copy all relevant configuration from the given other factory.
     * <p>Should include all standard configuration settings as well as
     * BeanPostProcessors, Scopes, and factory-specific internal settings.
     * Should not include any metadata of actual beans definitions,
     * such as BeanDefinition objects and beans name aliases.
     * @param otherFactory the other BeanFactory to copy from
     */
    void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

    /**
     * Given a beans name, create an alias. We typically use this method to
     * support names that are illegal within XML ids (used for beans names).
     * <p>Typically invoked during factory configuration, but can also be
     * used for runtime registration of aliases. Therefore, a factory
     * implementation should synchronize alias access.
     * @param beanName the canonical name of the target beans
     * @param alias the alias to be registered for the beans
     * @throws BeanDefinitionStoreException if the alias is already in use
     */
    void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

    /**
     * Resolve all alias target names and aliases registered in this
     * factory, applying the given StringValueResolver to them.
     * <p>The value resolver may for example resolve placeholders
     * in target beans names and even in alias names.
     * @param valueResolver the StringValueResolver to apply
     * @since 2.5
     */
    void resolveAliases(StringValueResolver valueResolver);

    /**
     * Return a merged BeanDefinition for the given beans name,
     * merging a child beans definition with its parent if necessary.
     * Considers beans definitions in ancestor factories as well.
     * @param beanName the name of the beans to retrieve the merged definition for
     * @return a (potentially merged) BeanDefinition for the given beans
     * @throws NoSuchBeanDefinitionException if there is no beans definition with the given name
     * @since 2.5
     */
    BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * Determine whether the beans with the given name is a FactoryBean.
     * @param name the name of the beans to check
     * @return whether the beans is a FactoryBean
     * ({@code false} means the beans exists but is not a FactoryBean)
     * @throws NoSuchBeanDefinitionException if there is no beans with the given name
     * @since 2.5
     */
    boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

    /**
     * Explicitly control the current in-creation status of the specified beans.
     * For container-internal use only.
     * @param beanName the name of the beans
     * @param inCreation whether the beans is currently in creation
     * @since 3.1
     */
    void setCurrentlyInCreation(String beanName, boolean inCreation);

    /**
     * Determine whether the specified beans is currently in creation.
     * @param beanName the name of the beans
     * @return whether the beans is currently in creation
     * @since 2.5
     */
    boolean isCurrentlyInCreation(String beanName);

    /**
     * Register a dependent beans for the given beans,
     * to be destroyed before the given beans is destroyed.
     * @param beanName the name of the beans
     * @param dependentBeanName the name of the dependent beans
     * @since 2.5
     */
    void registerDependentBean(String beanName, String dependentBeanName);

    /**
     * Return the names of all beans which depend on the specified beans, if any.
     * @param beanName the name of the beans
     * @return the array of dependent beans names, or an empty array if none
     * @since 2.5
     */
    String[] getDependentBeans(String beanName);

    /**
     * Return the names of all beans that the specified beans depends on, if any.
     * @param beanName the name of the beans
     * @return the array of names of beans which the beans depends on,
     * or an empty array if none
     * @since 2.5
     */
    String[] getDependenciesForBean(String beanName);

    /**
     * Destroy the given beans instance (usually a prototype instance
     * obtained from this factory) according to its beans definition.
     * <p>Any exception that arises during destruction should be caught
     * and logged instead of propagated to the caller of this method.
     * @param beanName the name of the beans definition
     * @param beanInstance the beans instance to destroy
     */
    void destroyBean(String beanName, Object beanInstance);

    /**
     * Destroy the specified scoped beans in the current target scope, if any.
     * <p>Any exception that arises during destruction should be caught
     * and logged instead of propagated to the caller of this method.
     * @param beanName the name of the scoped beans
     */
    void destroyScopedBean(String beanName);

    /**
     * Destroy all singleton beans in this factory, including inner beans that have
     * been registered as disposable. To be called on shutdown of a factory.
     * <p>Any exception that arises during destruction should be caught
     * and logged instead of propagated to the caller of this method.
     */
    void destroySingletons();

}
