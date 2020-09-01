package com.vick.test.util.jdbc.framework.io;

import com.vick.test.util.jdbc.framework.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
abstract class VfsPatternUtils extends VfsUtils {

    @Nullable
    static Object getVisitorAttributes() {
        return doGetVisitorAttributes();
    }

    static String getPath(Object resource) {
        String path = doGetPath(resource);
        return (path != null ? path : "");
    }

    static Object findRoot(URL url) throws IOException {
        return getRoot(url);
    }

    static void visit(Object resource, InvocationHandler visitor) throws IOException {
        Object visitorProxy = Proxy.newProxyInstance(
                VIRTUAL_FILE_VISITOR_INTERFACE.getClassLoader(),
                new Class<?>[] {VIRTUAL_FILE_VISITOR_INTERFACE}, visitor);
        invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, visitorProxy);
    }

}
