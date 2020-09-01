package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@FunctionalInterface
public interface NamespaceHandlerResolver {

    /**
     * Resolve the namespace URI and return the located {link NamespaceHandler}
     * implementation.
     * @param namespaceUri the relevant namespace URI
     * @return the located {link NamespaceHandler} (may be {@code null})
     */
    @Nullable
    NamespaceHandler resolve(String namespaceUri);

}
