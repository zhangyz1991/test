package com.vick.test.util.jdbc.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class PrioritizedParameterNameDiscoverer implements ParameterNameDiscoverer {

    private final List<ParameterNameDiscoverer> parameterNameDiscoverers = new LinkedList<>();


    /**
     * Add a further {@link ParameterNameDiscoverer} delegate to the list of
     * discoverers that this {@code PrioritizedParameterNameDiscoverer} checks.
     */
    public void addDiscoverer(ParameterNameDiscoverer pnd) {
        this.parameterNameDiscoverers.add(pnd);
    }


    @Override
    @Nullable
    public String[] getParameterNames(Method method) {
        for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
            String[] result = pnd.getParameterNames(method);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public String[] getParameterNames(Constructor<?> ctor) {
        for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
            String[] result = pnd.getParameterNames(ctor);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
