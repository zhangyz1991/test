package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DefaultBeanNameGenerator implements BeanNameGenerator {

    /**
     * A convenient constant for a default {@code DefaultBeanNameGenerator} instance,
     * as used for {@link AbstractBeanDefinitionReader} setup.
     * @since 5.2
     */
    public static final DefaultBeanNameGenerator INSTANCE = new DefaultBeanNameGenerator();


    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
    }

}
