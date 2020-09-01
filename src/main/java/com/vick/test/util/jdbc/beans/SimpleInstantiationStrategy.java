package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.StringUtils;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<>();


    /**
     * Return the factory method currently being invoked or {@code null} if none.
     * <p>Allows factory method implementations to determine whether the current
     * caller is the container itself as opposed to user code.
     */
    @Nullable
    public static Method getCurrentlyInvokedFactoryMethod() {
        return currentlyInvokedFactoryMethod.get();
    }


    @Override
    public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
        // Don't override the class with CGLIB if no overrides.
        if (!bd.hasMethodOverrides()) {
            Constructor<?> constructorToUse;
            synchronized (bd.constructorArgumentLock) {
                constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
                if (constructorToUse == null) {
                    final Class<?> clazz = bd.getBeanClass();
                    if (clazz.isInterface()) {
                        throw new BeanInstantiationException(clazz, "Specified class is an interface");
                    }
                    try {
                        if (System.getSecurityManager() != null) {
                            constructorToUse = AccessController.doPrivileged(
                                    (PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
                        }
                        else {
                            constructorToUse = clazz.getDeclaredConstructor();
                        }
                        bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                    }
                    catch (Throwable ex) {
                        throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                    }
                }
            }
            return BeanUtils.instantiateClass(constructorToUse);
        }
        else {
            // Must generate CGLIB subclass.
            return instantiateWithMethodInjection(bd, beanName, owner);
        }
    }

    /**
     * Subclasses can override this method, which is implemented to throw
     * UnsupportedOperationException, if they can instantiate an object with
     * the Method Injection specified in the given RootBeanDefinition.
     * Instantiation should use a no-arg constructor.
     */
    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
        throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
    }

    @Override
    public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
                              final Constructor<?> ctor, Object... args) {

        if (!bd.hasMethodOverrides()) {
            if (System.getSecurityManager() != null) {
                // use own privileged to change accessibility (when security is on)
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ReflectionUtils.makeAccessible(ctor);
                    return null;
                });
            }
            return BeanUtils.instantiateClass(ctor, args);
        }
        else {
            return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
        }
    }

    /**
     * Subclasses can override this method, which is implemented to throw
     * UnsupportedOperationException, if they can instantiate an object with
     * the Method Injection specified in the given RootBeanDefinition.
     * Instantiation should use the given constructor and parameters.
     */
    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName,
                                                    BeanFactory owner, @Nullable Constructor<?> ctor, Object... args) {

        throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
    }

    @Override
    public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
                              @Nullable Object factoryBean, final Method factoryMethod, Object... args) {

        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    ReflectionUtils.makeAccessible(factoryMethod);
                    return null;
                });
            }
            else {
                ReflectionUtils.makeAccessible(factoryMethod);
            }

            Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
            try {
                currentlyInvokedFactoryMethod.set(factoryMethod);
                Object result = factoryMethod.invoke(factoryBean, args);
                if (result == null) {
                    result = new NullBean();
                }
                return result;
            }
            finally {
                if (priorInvokedFactoryMethod != null) {
                    currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
                }
                else {
                    currentlyInvokedFactoryMethod.remove();
                }
            }
        }
        catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException(factoryMethod,
                    "Illegal arguments to factory method '" + factoryMethod.getName() + "'; " +
                            "args: " + StringUtils.arrayToCommaDelimitedString(args), ex);
        }
        catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(factoryMethod,
                    "Cannot access factory method '" + factoryMethod.getName() + "'; is it public?", ex);
        }
        catch (InvocationTargetException ex) {
            String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
            if (bd.getFactoryBeanName() != null && owner instanceof ConfigurableBeanFactory &&
                    ((ConfigurableBeanFactory) owner).isCurrentlyInCreation(bd.getFactoryBeanName())) {
                msg = "Circular reference involving containing beans '" + bd.getFactoryBeanName() + "' - consider " +
                        "declaring the factory method as static for independence from its containing instance. " + msg;
            }
            throw new BeanInstantiationException(factoryMethod, msg, ex.getTargetException());
        }
    }

}
