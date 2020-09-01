package com.vick.test.util.jdbc.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class KotlinReflectionParameterNameDiscoverer implements ParameterNameDiscoverer {

    @Override
    @Nullable
    public String[] getParameterNames(Method method) {
        if (!KotlinDetector.isKotlinType(method.getDeclaringClass())) {
            return null;
        }

        //此处注释了
        /*try {
            KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
            return (function != null ? getParameterNames(function.getParameters()) : null);
        }
        catch (UnsupportedOperationException ex) {
            return null;
        }*/
        return null;
    }

    @Override
    @Nullable
    public String[] getParameterNames(Constructor<?> ctor) {
        if (ctor.getDeclaringClass().isEnum() || !KotlinDetector.isKotlinType(ctor.getDeclaringClass())) {
            return null;
        }

        //此处注释了
        /*try {
            KFunction<?> function = ReflectJvmMapping.getKotlinFunction(ctor);
            return (function != null ? getParameterNames(function.getParameters()) : null);
        }
        catch (UnsupportedOperationException ex) {
            return null;
        }*/
        return null;
    }

    //此处注释了
    /*@Nullable
    private String[] getParameterNames(List<KParameter> parameters) {
        List<KParameter> filteredParameters = parameters
                .stream()
                // Extension receivers of extension methods must be included as they appear as normal method parameters in Java
                .filter(p -> KParameter.Kind.VALUE.equals(p.getKind()) || KParameter.Kind.EXTENSION_RECEIVER.equals(p.getKind()))
                .collect(Collectors.toList());
        String[] parameterNames = new String[filteredParameters.size()];
        for (int i = 0; i < filteredParameters.size(); i++) {
            KParameter parameter = filteredParameters.get(i);
            // extension receivers are not explicitly named, but require a name for Java interoperability
            // $receiver is not a valid Kotlin identifier, but valid in Java, so it can be used here
            String name = KParameter.Kind.EXTENSION_RECEIVER.equals(parameter.getKind())  ? "$receiver" : parameter.getName();
            if (name == null) {
                return null;
            }
            parameterNames[i] = name;
        }
        return parameterNames;
    }*/
    @Nullable
    private String[] getParameterNames(List parameters) {
        return null;
    }

}
