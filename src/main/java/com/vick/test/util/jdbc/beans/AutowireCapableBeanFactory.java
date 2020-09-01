package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.util.Set;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * Constant that indicates no externally defined autowiring. Note that
     * BeanFactoryAware etc and annotation-driven injection will still be applied.
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_NO = 0;

    /**
     * Constant that indicates autowiring beans properties by name
     * (applying to all beans property setters).
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_BY_NAME = 1;

    /**
     * Constant that indicates autowiring beans properties by type
     * (applying to all beans property setters).
     * @see #createBean
     * @see #autowire
     * @see #autowireBeanProperties
     */
    int AUTOWIRE_BY_TYPE = 2;

    /**
     * Constant that indicates autowiring the greediest constructor that
     * can be satisfied (involves resolving the appropriate constructor).
     * @see #createBean
     * @see #autowire
     */
    int AUTOWIRE_CONSTRUCTOR = 3;

    /**
     * Constant that indicates determining an appropriate autowire strategy
     * through introspection of the beans class.
     * @see #createBean
     * @see #autowire
     * @deprecated as of Spring 3.0: If you are using mixed autowiring strategies,
     * prefer annotation-based autowiring for clearer demarcation of autowiring needs.
     */
    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    /**
     * Suffix for the "original instance" convention when initializing an existing
     * beans instance: to be appended to the fully-qualified beans class name,
     * e.g. "com.mypackage.MyClass.ORIGINAL", in order to enforce the given instance
     * to be returned, i.e. no proxies etc.
     * @since 5.1
     * @see #initializeBean(Object, String)
     * @see #applyBeanPostProcessorsBeforeInitialization(Object, String)
     * @see #applyBeanPostProcessorsAfterInitialization(Object, String)
     */
    String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";


    //-------------------------------------------------------------------------
    // Typical methods for creating and populating external beans instances
    //-------------------------------------------------------------------------

    /**
     * Fully create a new beans instance of the given class.
     * <p>Performs full initialization of the beans, including all applicable
     * {@link BeanPostProcessor BeanPostProcessors}.
     * <p>Note: This is intended for creating a fresh instance, populating annotated
     * fields and methods as well as applying all standard beans initialization callbacks.
     * It does <i>not</i> imply traditional by-name or by-type autowiring of properties;
     * use {@link #createBean(Class, int, boolean)} for those purposes.
     * @param beanClass the class of the beans to create
     * @return the new beans instance
     * @throws BeansException if instantiation or wiring failed
     */
    <T> T createBean(Class<T> beanClass) throws BeansException;

    /**
     * Populate the given beans instance through applying after-instantiation callbacks
     * and beans property post-processing (e.g. for annotation-driven injection).
     * <p>Note: This is essentially intended for (re-)populating annotated fields and
     * methods, either for new instances or for deserialized instances. It does
     * <i>not</i> imply traditional by-name or by-type autowiring of properties;
     * use {@link #autowireBeanProperties} for those purposes.
     * @param existingBean the existing beans instance
     * @throws BeansException if wiring failed
     */
    void autowireBean(Object existingBean) throws BeansException;

    /**
     * Configure the given raw beans: autowiring beans properties, applying
     * beans property values, applying factory callbacks such as {@code setBeanName}
     * and {@code setBeanFactory}, and also applying all beans post processors
     * (including ones which might wrap the given raw beans).
     * <p>This is effectively a superset of what {@link #initializeBean} provides,
     * fully applying the configuration specified by the corresponding beans definition.
     * <b>Note: This method requires a beans definition for the given name!</b>
     * @param existingBean the existing beans instance
     * @param beanName the name of the beans, to be passed to it if necessary
     * (a beans definition of that name has to be available)
     * @return the beans instance to use, either the original or a wrapped one
     * throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     * if there is no beans definition with the given name
     * @throws BeansException if the initialization failed
     * @see #initializeBean
     */
    Object configureBean(Object existingBean, String beanName) throws BeansException;


    //-------------------------------------------------------------------------
    // Specialized methods for fine-grained control over the beans lifecycle
    //-------------------------------------------------------------------------

    /**
     * Fully create a new beans instance of the given class with the specified
     * autowire strategy. All constants defined in this interface are supported here.
     * <p>Performs full initialization of the beans, including all applicable
     * {@link BeanPostProcessor BeanPostProcessors}. This is effectively a superset
     * of what {@link #autowire} provides, adding {@link #initializeBean} behavior.
     * @param beanClass the class of the beans to create
     * @param autowireMode by name or type, using the constants in this interface
     * @param dependencyCheck whether to perform a dependency check for objects
     * (not applicable to autowiring a constructor, thus ignored there)
     * @return the new beans instance
     * @throws BeansException if instantiation or wiring failed
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     */
    Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

    /**
     * Instantiate a new beans instance of the given class with the specified autowire
     * strategy. All constants defined in this interface are supported here.
     * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
     * before-instantiation callbacks (e.g. for annotation-driven injection).
     * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
     * callbacks or perform any further initialization of the beans. This interface
     * offers distinct, fine-grained operations for those purposes, for example
     * {@link #initializeBean}. However, {link InstantiationAwareBeanPostProcessor}
     * callbacks are applied, if applicable to the construction of the instance.
     * @param beanClass the class of the beans to instantiate
     * @param autowireMode by name or type, using the constants in this interface
     * @param dependencyCheck whether to perform a dependency check for object
     * references in the beans instance (not applicable to autowiring a constructor,
     * thus ignored there)
     * @return the new beans instance
     * @throws BeansException if instantiation or wiring failed
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     * @see #initializeBean
     * @see #applyBeanPostProcessorsBeforeInitialization
     * @see #applyBeanPostProcessorsAfterInitialization
     */
    Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

    /**
     * Autowire the beans properties of the given beans instance by name or type.
     * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
     * after-instantiation callbacks (e.g. for annotation-driven injection).
     * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
     * callbacks or perform any further initialization of the beans. This interface
     * offers distinct, fine-grained operations for those purposes, for example
     * {@link #initializeBean}. However, {link InstantiationAwareBeanPostProcessor}
     * callbacks are applied, if applicable to the configuration of the instance.
     * @param existingBean the existing beans instance
     * @param autowireMode by name or type, using the constants in this interface
     * @param dependencyCheck whether to perform a dependency check for object
     * references in the beans instance
     * @throws BeansException if wiring failed
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_NO
     */
    void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
            throws BeansException;

    /**
     * Apply the property values of the beans definition with the given name to
     * the given beans instance. The beans definition can either define a fully
     * self-contained beans, reusing its property values, or just property values
     * meant to be used for existing beans instances.
     * <p>This method does <i>not</i> autowire beans properties; it just applies
     * explicitly defined property values. Use the {@link #autowireBeanProperties}
     * method to autowire an existing beans instance.
     * <b>Note: This method requires a beans definition for the given name!</b>
     * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
     * callbacks or perform any further initialization of the beans. This interface
     * offers distinct, fine-grained operations for those purposes, for example
     * {@link #initializeBean}. However, {link InstantiationAwareBeanPostProcessor}
     * callbacks are applied, if applicable to the configuration of the instance.
     * @param existingBean the existing beans instance
     * @param beanName the name of the beans definition in the beans factory
     * (a beans definition of that name has to be available)
     * throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     * if there is no beans definition with the given name
     * @throws BeansException if applying the property values failed
     * @see #autowireBeanProperties
     */
    void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;

    /**
     * Initialize the given raw beans, applying factory callbacks
     * such as {@code setBeanName} and {@code setBeanFactory},
     * also applying all beans post processors (including ones which
     * might wrap the given raw beans).
     * <p>Note that no beans definition of the given name has to exist
     * in the beans factory. The passed-in beans name will simply be used
     * for callbacks but not checked against the registered beans definitions.
     * @param existingBean the existing beans instance
     * @param beanName the name of the beans, to be passed to it if necessary
     * (only passed to {@link BeanPostProcessor BeanPostProcessors};
     * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
     * enforce the given instance to be returned, i.e. no proxies etc)
     * @return the beans instance to use, either the original or a wrapped one
     * @throws BeansException if the initialization failed
     * @see #ORIGINAL_INSTANCE_SUFFIX
     */
    Object initializeBean(Object existingBean, String beanName) throws BeansException;

    /**
     * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing beans
     * instance, invoking their {@code postProcessBeforeInitialization} methods.
     * The returned beans instance may be a wrapper around the original.
     * @param existingBean the existing beans instance
     * @param beanName the name of the beans, to be passed to it if necessary
     * (only passed to {@link BeanPostProcessor BeanPostProcessors};
     * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
     * enforce the given instance to be returned, i.e. no proxies etc)
     * @return the beans instance to use, either the original or a wrapped one
     * @throws BeansException if any post-processing failed
     * @see BeanPostProcessor#postProcessBeforeInitialization
     * @see #ORIGINAL_INSTANCE_SUFFIX
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    /**
     * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing beans
     * instance, invoking their {@code postProcessAfterInitialization} methods.
     * The returned beans instance may be a wrapper around the original.
     * @param existingBean the existing beans instance
     * @param beanName the name of the beans, to be passed to it if necessary
     * (only passed to {@link BeanPostProcessor BeanPostProcessors};
     * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
     * enforce the given instance to be returned, i.e. no proxies etc)
     * @return the beans instance to use, either the original or a wrapped one
     * @throws BeansException if any post-processing failed
     * @see BeanPostProcessor#postProcessAfterInitialization
     * @see #ORIGINAL_INSTANCE_SUFFIX
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;

    /**
     * Destroy the given beans instance (typically coming from {@link #createBean}),
     * applying the {link org.springframework.beans.factory.DisposableBean} contract as well as
     * registered {link DestructionAwareBeanPostProcessor DestructionAwareBeanPostProcessors}.
     * <p>Any exception that arises during destruction should be caught
     * and logged instead of propagated to the caller of this method.
     * @param existingBean the beans instance to destroy
     */
    void destroyBean(Object existingBean);


    //-------------------------------------------------------------------------
    // Delegate methods for resolving injection points
    //-------------------------------------------------------------------------

    /**
     * Resolve the beans instance that uniquely matches the given object type, if any,
     * including its beans name.
     * <p>This is effectively a variant of {@link #getBean(Class)} which preserves the
     * beans name of the matching instance.
     * @param requiredType type the beans must match; can be an interface or superclass
     * @return the beans name plus beans instance
     * @throws NoSuchBeanDefinitionException if no matching beans was found
     * @throws NoUniqueBeanDefinitionException if more than one matching beans was found
     * @throws BeansException if the beans could not be created
     * @since 4.3.3
     * @see #getBean(Class)
     */
    <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException;

    /**
     * Resolve a beans instance for the given beans name, providing a dependency descriptor
     * for exposure to target factory methods.
     * <p>This is effectively a variant of {@link #getBean(String, Class)} which supports
     * factory methods with an {link org.springframework.beans.factory.InjectionPoint}
     * argument.
     * @param name the name of the beans to look up
     * @param descriptor the dependency descriptor for the requesting injection point
     * @return the corresponding beans instance
     * @throws NoSuchBeanDefinitionException if there is no beans with the specified name
     * @throws BeansException if the beans could not be created
     * @since 5.1.5
     * @see #getBean(String, Class)
     */
    Object resolveBeanByName(String name, DependencyDescriptor descriptor) throws BeansException;

    /**
     * Resolve the specified dependency against the beans defined in this factory.
     * @param descriptor the descriptor for the dependency (field/method/constructor)
     * @param requestingBeanName the name of the beans which declares the given dependency
     * @return the resolved object, or {@code null} if none found
     * @throws NoSuchBeanDefinitionException if no matching beans was found
     * @throws NoUniqueBeanDefinitionException if more than one matching beans was found
     * @throws BeansException if dependency resolution failed for any other reason
     * @since 2.5
     * @see #resolveDependency(DependencyDescriptor, String, Set, TypeConverter)
     */
    @Nullable
    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName) throws BeansException;

    /**
     * Resolve the specified dependency against the beans defined in this factory.
     * @param descriptor the descriptor for the dependency (field/method/constructor)
     * @param requestingBeanName the name of the beans which declares the given dependency
     * @param autowiredBeanNames a Set that all names of autowired beans (used for
     * resolving the given dependency) are supposed to be added to
     * @param typeConverter the TypeConverter to use for populating arrays and collections
     * @return the resolved object, or {@code null} if none found
     * @throws NoSuchBeanDefinitionException if no matching beans was found
     * @throws NoUniqueBeanDefinitionException if more than one matching beans was found
     * @throws BeansException if dependency resolution failed for any other reason
     * @since 2.5
     * @see DependencyDescriptor
     */
    @Nullable
    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,
                             @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException;

}
