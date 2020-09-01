package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface DisposableBean {

    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a beans.
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     * but not rethrown to allow other beans to release their resources as well.
     */
    void destroy() throws Exception;

}
