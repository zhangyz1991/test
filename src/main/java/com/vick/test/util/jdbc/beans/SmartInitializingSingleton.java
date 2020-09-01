package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface SmartInitializingSingleton {

    /**
     * Invoked right at the end of the singleton pre-instantiation phase,
     * with a guarantee that all regular singleton beans have been created
     * already. {@link ListableBeanFactory#getBeansOfType} calls within
     * this method won't trigger accidental side effects during bootstrap.
     * <p><b>NOTE:</b> This callback won't be triggered for singleton beans
     * lazily initialized on demand after {@link BeanFactory} bootstrap,
     * and not for any other bean scope either. Carefully use it for beans
     * with the intended bootstrap semantics only.
     */
    void afterSingletonsInstantiated();

}
