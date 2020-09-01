package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * Apply this BeanPostProcessor to the given beans instance before its
     * destruction, e.g. invoking custom destruction callbacks.
     * <p>Like DisposableBean's {@code destroy} and a custom destroy method, this
     * callback will only apply to beans which the container fully manages the
     * lifecycle for. This is usually the case for singletons and scoped beans.
     * @param bean the beans instance to be destroyed
     * @param beanName the name of the beans
     * throws org.springframework.beans.BeansException in case of errors
     * see org.springframework.beans.factory.DisposableBean#destroy()
     * see org.springframework.beans.factory.support.AbstractBeanDefinition#setDestroyMethodName(String)
     */
    void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;

    /**
     * Determine whether the given beans instance requires destruction by this
     * post-processor.
     * <p>The default implementation returns {@code true}. If a pre-5 implementation
     * of {@code DestructionAwareBeanPostProcessor} does not provide a concrete
     * implementation of this method, Spring silently assumes {@code true} as well.
     * @param bean the beans instance to check
     * @return {@code true} if {@link #postProcessBeforeDestruction} is supposed to
     * be called for this beans instance eventually, or {@code false} if not needed
     * @since 4.3
     */
    default boolean requiresDestruction(Object bean) {
        return true;
    }

}
