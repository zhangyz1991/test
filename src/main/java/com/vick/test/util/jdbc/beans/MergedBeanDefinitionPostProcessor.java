package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {

    /**
     * Post-process the given merged bean definition for the specified bean.
     * @param beanDefinition the merged bean definition for the bean
     * @param beanType the actual type of the managed bean instance
     * @param beanName the name of the bean
     * @see AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors
     */
    void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName);

    /**
     * A notification that the bean definition for the specified name has been reset,
     * and that this post-processor should clear any metadata for the affected bean.
     * <p>The default implementation is empty.
     * @param beanName the name of the bean
     * @since 5.1
     * see DefaultListableBeanFactory#resetBeanDefinition
     */
    default void resetBeanDefinition(String beanName) {
    }

}
