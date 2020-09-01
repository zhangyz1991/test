package com.vick.test.util.jdbc.beans;

import java.security.AccessControlContext;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface SecurityContextProvider {

    /**
     * Provides a security access control context relevant to a beans factory.
     * @return beans factory security control context
     */
    AccessControlContext getAccessControlContext();

}
