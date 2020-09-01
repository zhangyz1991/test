package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface InstantiationStrategy {

    /**
     * Return an instance of the beans with the given name in this factory.
     * @param bd the beans definition
     * @param beanName the name of the beans when it is created in this context.
     * The name can be {@code null} if we are autowiring a beans which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @return a beans instance for this beans definition
     * @throws BeansException if the instantiation attempt failed
     */
    Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner)
            throws BeansException;

    /**
     * Return an instance of the beans with the given name in this factory,
     * creating it via the given constructor.
     * @param bd the beans definition
     * @param beanName the name of the beans when it is created in this context.
     * The name can be {@code null} if we are autowiring a beans which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @param ctor the constructor to use
     * @param args the constructor arguments to apply
     * @return a beans instance for this beans definition
     * @throws BeansException if the instantiation attempt failed
     */
    Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
                       Constructor<?> ctor, Object... args) throws BeansException;

    /**
     * Return an instance of the beans with the given name in this factory,
     * creating it via the given factory method.
     * @param bd the beans definition
     * @param beanName the name of the beans when it is created in this context.
     * The name can be {@code null} if we are autowiring a beans which doesn't
     * belong to the factory.
     * @param owner the owning BeanFactory
     * @param factoryBean the factory beans instance to call the factory method on,
     * or {@code null} in case of a static factory method
     * @param factoryMethod the factory method to use
     * @param args the factory method arguments to apply
     * @return a beans instance for this beans definition
     * @throws BeansException if the instantiation attempt failed
     */
    Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner,
                       @Nullable Object factoryBean, Method factoryMethod, Object... args)
            throws BeansException;

}
