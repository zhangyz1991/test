package com.vick.test.util.jdbc.framework.io;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface ContextResource extends Resource {

    /**
     * Return the path within the enclosing 'context'.
     * <p>This is typically path relative to a context-specific root directory,
     * e.g. a ServletContext root or a PortletContext root.
     */
    String getPathWithinContext();

}
