package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
@SuppressWarnings("serial")
class DisposableBeanAdapter implements DisposableBean, Runnable, Serializable {

    private static final String CLOSE_METHOD_NAME = "close";

    private static final String SHUTDOWN_METHOD_NAME = "shutdown";

    private static final Logger logger = LoggerFactory.getLogger(DisposableBeanAdapter.class);


    private final Object bean;

    private final String beanName;

    private final boolean invokeDisposableBean;

    private final boolean nonPublicAccessAllowed;

    @Nullable
    private final AccessControlContext acc;

    @Nullable
    private String destroyMethodName;

    @Nullable
    private transient Method destroyMethod;

    @Nullable
    private List<DestructionAwareBeanPostProcessor> beanPostProcessors;


    /**
     * Create a new DisposableBeanAdapter for the given beans.
     * @param bean the beans instance (never {@code null})
     * @param beanName the name of the beans
     * @param beanDefinition the merged beans definition
     * @param postProcessors the List of BeanPostProcessors
     * (potentially DestructionAwareBeanPostProcessor), if any
     */
    public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition,
                                 List<BeanPostProcessor> postProcessors, @Nullable AccessControlContext acc) {

        Assert.notNull(bean, "Disposable beans must not be null");
        this.bean = bean;
        this.beanName = beanName;
        this.invokeDisposableBean =
                (this.bean instanceof DisposableBean && !beanDefinition.isExternallyManagedDestroyMethod("destroy"));
        this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
        this.acc = acc;
        String destroyMethodName = inferDestroyMethodIfNecessary(bean, beanDefinition);
        if (destroyMethodName != null && !(this.invokeDisposableBean && "destroy".equals(destroyMethodName)) &&
                !beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)) {
            this.destroyMethodName = destroyMethodName;
            Method destroyMethod = determineDestroyMethod(destroyMethodName);
            if (destroyMethod == null) {
                if (beanDefinition.isEnforceDestroyMethod()) {
                    throw new BeanDefinitionValidationException("Could not find a destroy method named '" +
                            destroyMethodName + "' on beans with name '" + beanName + "'");
                }
            }
            else {
                Class<?>[] paramTypes = destroyMethod.getParameterTypes();
                if (paramTypes.length > 1) {
                    throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of beans '" +
                            beanName + "' has more than one parameter - not supported as destroy method");
                }
                else if (paramTypes.length == 1 && boolean.class != paramTypes[0]) {
                    throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of beans '" +
                            beanName + "' has a non-boolean parameter - not supported as destroy method");
                }
                destroyMethod = ClassUtils.getInterfaceMethodIfPossible(destroyMethod);
            }
            this.destroyMethod = destroyMethod;
        }
        this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
    }

    /**
     * Create a new DisposableBeanAdapter for the given beans.
     * @param bean the beans instance (never {@code null})
     * @param postProcessors the List of BeanPostProcessors
     * (potentially DestructionAwareBeanPostProcessor), if any
     */
    public DisposableBeanAdapter(Object bean, List<BeanPostProcessor> postProcessors, AccessControlContext acc) {
        Assert.notNull(bean, "Disposable beans must not be null");
        this.bean = bean;
        this.beanName = bean.getClass().getName();
        this.invokeDisposableBean = (this.bean instanceof DisposableBean);
        this.nonPublicAccessAllowed = true;
        this.acc = acc;
        this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
    }

    /**
     * Create a new DisposableBeanAdapter for the given beans.
     */
    private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean,
                                  boolean nonPublicAccessAllowed, @Nullable String destroyMethodName,
                                  @Nullable List<DestructionAwareBeanPostProcessor> postProcessors) {

        this.bean = bean;
        this.beanName = beanName;
        this.invokeDisposableBean = invokeDisposableBean;
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
        this.acc = null;
        this.destroyMethodName = destroyMethodName;
        this.beanPostProcessors = postProcessors;
    }


    /**
     * If the current value of the given beanDefinition's "destroyMethodName" property is
     * {@link AbstractBeanDefinition#INFER_METHOD}, then attempt to infer a destroy method.
     * Candidate methods are currently limited to public, no-arg methods named "close" or
     * "shutdown" (whether declared locally or inherited). The given BeanDefinition's
     * "destroyMethodName" is updated to be null if no such method is found, otherwise set
     * to the name of the inferred method. This constant serves as the default for the
     * {@code @Bean#destroyMethod} attribute and the value of the constant may also be
     * used in XML within the {@code <beans destroy-method="">} or {@code
     * <beans default-destroy-method="">} attributes.
     * <p>Also processes the {@link java.io.Closeable} and {@link java.lang.AutoCloseable}
     * interfaces, reflectively calling the "close" method on implementing beans as well.
     */
    @Nullable
    private String inferDestroyMethodIfNecessary(Object bean, RootBeanDefinition beanDefinition) {
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (AbstractBeanDefinition.INFER_METHOD.equals(destroyMethodName) ||
                (destroyMethodName == null && bean instanceof AutoCloseable)) {
            // Only perform destroy method inference or Closeable detection
            // in case of the beans not explicitly implementing DisposableBean
            if (!(bean instanceof DisposableBean)) {
                try {
                    return bean.getClass().getMethod(CLOSE_METHOD_NAME).getName();
                }
                catch (NoSuchMethodException ex) {
                    try {
                        return bean.getClass().getMethod(SHUTDOWN_METHOD_NAME).getName();
                    }
                    catch (NoSuchMethodException ex2) {
                        // no candidate destroy method found
                    }
                }
            }
            return null;
        }
        return (StringUtils.hasLength(destroyMethodName) ? destroyMethodName : null);
    }

    /**
     * Search for all DestructionAwareBeanPostProcessors in the List.
     * @param processors the List to search
     * @return the filtered List of DestructionAwareBeanPostProcessors
     */
    @Nullable
    private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> processors, Object bean) {
        List<DestructionAwareBeanPostProcessor> filteredPostProcessors = null;
        if (!CollectionUtils.isEmpty(processors)) {
            filteredPostProcessors = new ArrayList<>(processors.size());
            for (BeanPostProcessor processor : processors) {
                if (processor instanceof DestructionAwareBeanPostProcessor) {
                    DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor) processor;
                    if (dabpp.requiresDestruction(bean)) {
                        filteredPostProcessors.add(dabpp);
                    }
                }
            }
        }
        return filteredPostProcessors;
    }


    @Override
    public void run() {
        destroy();
    }

    @Override
    public void destroy() {
        if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
            for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
                processor.postProcessBeforeDestruction(this.bean, this.beanName);
            }
        }

        if (this.invokeDisposableBean) {
            if (logger.isTraceEnabled()) {
                logger.trace("Invoking destroy() on beans with name '" + this.beanName + "'");
            }
            try {
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                        ((DisposableBean) this.bean).destroy();
                        return null;
                    }, this.acc);
                }
                else {
                    ((DisposableBean) this.bean).destroy();
                }
            }
            catch (Throwable ex) {
                String msg = "Invocation of destroy method failed on beans with name '" + this.beanName + "'";
                if (logger.isDebugEnabled()) {
                    logger.warn(msg, ex);
                }
                else {
                    logger.warn(msg + ": " + ex);
                }
            }
        }

        if (this.destroyMethod != null) {
            invokeCustomDestroyMethod(this.destroyMethod);
        }
        else if (this.destroyMethodName != null) {
            Method methodToInvoke = determineDestroyMethod(this.destroyMethodName);
            if (methodToInvoke != null) {
                invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(methodToInvoke));
            }
        }
    }


    @Nullable
    private Method determineDestroyMethod(String name) {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged((PrivilegedAction<Method>) () -> findDestroyMethod(name));
            }
            else {
                return findDestroyMethod(name);
            }
        }
        catch (IllegalArgumentException ex) {
            throw new BeanDefinitionValidationException("Could not find unique destroy method on beans with name '" +
                    this.beanName + ": " + ex.getMessage());
        }
    }

    @Nullable
    private Method findDestroyMethod(String name) {
        return (this.nonPublicAccessAllowed ?
                BeanUtils.findMethodWithMinimalParameters(this.bean.getClass(), name) :
                BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), name));
    }

    /**
     * Invoke the specified custom destroy method on the given beans.
     * <p>This implementation invokes a no-arg method if found, else checking
     * for a method with a single boolean argument (passing in "true",
     * assuming a "force" parameter), else logging an error.
     */
    private void invokeCustomDestroyMethod(final Method destroyMethod) {
        int paramCount = destroyMethod.getParameterCount();
        final Object[] args = new Object[paramCount];
        if (paramCount == 1) {
            args[0] = Boolean.TRUE;
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Invoking destroy method '" + this.destroyMethodName +
                    "' on beans with name '" + this.beanName + "'");
        }
        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ReflectionUtils.makeAccessible(destroyMethod);
                    return null;
                });
                try {
                    AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                            destroyMethod.invoke(this.bean, args), this.acc);
                }
                catch (PrivilegedActionException pax) {
                    throw (InvocationTargetException) pax.getException();
                }
            }
            else {
                ReflectionUtils.makeAccessible(destroyMethod);
                destroyMethod.invoke(this.bean, args);
            }
        }
        catch (InvocationTargetException ex) {
            String msg = "Destroy method '" + this.destroyMethodName + "' on beans with name '" +
                    this.beanName + "' threw an exception";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex.getTargetException());
            }
            else {
                logger.warn(msg + ": " + ex.getTargetException());
            }
        }
        catch (Throwable ex) {
            logger.warn("Failed to invoke destroy method '" + this.destroyMethodName +
                    "' on beans with name '" + this.beanName + "'", ex);
        }
    }


    /**
     * Serializes a copy of the state of this class,
     * filtering out non-serializable BeanPostProcessors.
     */
    protected Object writeReplace() {
        List<DestructionAwareBeanPostProcessor> serializablePostProcessors = null;
        if (this.beanPostProcessors != null) {
            serializablePostProcessors = new ArrayList<>();
            for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
                if (postProcessor instanceof Serializable) {
                    serializablePostProcessors.add(postProcessor);
                }
            }
        }
        return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean,
                this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
    }


    /**
     * Check whether the given beans has any kind of destroy method to call.
     * @param bean the beans instance
     * @param beanDefinition the corresponding beans definition
     */
    public static boolean hasDestroyMethod(Object bean, RootBeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || bean instanceof AutoCloseable) {
            return true;
        }
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (AbstractBeanDefinition.INFER_METHOD.equals(destroyMethodName)) {
            return (ClassUtils.hasMethod(bean.getClass(), CLOSE_METHOD_NAME) ||
                    ClassUtils.hasMethod(bean.getClass(), SHUTDOWN_METHOD_NAME));
        }
        return StringUtils.hasLength(destroyMethodName);
    }

    /**
     * Check whether the given beans has destruction-aware post-processors applying to it.
     * @param bean the beans instance
     * @param postProcessors the post-processor candidates
     */
    public static boolean hasApplicableProcessors(Object bean, List<BeanPostProcessor> postProcessors) {
        if (!CollectionUtils.isEmpty(postProcessors)) {
            for (BeanPostProcessor processor : postProcessors) {
                if (processor instanceof DestructionAwareBeanPostProcessor) {
                    DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor) processor;
                    if (dabpp.requiresDestruction(bean)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}