package com.vick.test.util.jdbc.cglib;

import java.util.*;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class CustomizerRegistry {
    private final Class[] customizerTypes;
    private Map<Class, List<KeyFactoryCustomizer>> customizers = new HashMap();

    public CustomizerRegistry(Class[] customizerTypes) {
        this.customizerTypes = customizerTypes;
    }

    public void add(KeyFactoryCustomizer customizer) {
        Class<? extends KeyFactoryCustomizer> klass = customizer.getClass();
        Class[] var3 = this.customizerTypes;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Class type = var3[var5];
            if (type.isAssignableFrom(klass)) {
                List<KeyFactoryCustomizer> list = (List)this.customizers.get(type);
                if (list == null) {
                    this.customizers.put(type, list = new ArrayList());
                }

                ((List)list).add(customizer);
            }
        }

    }

    public <T> List<T> get(Class<T> klass) {
        List list = (List)this.customizers.get(klass);
        return list == null ? Collections.emptyList() : list;
    }

    /** @deprecated */
    @Deprecated
    public static CustomizerRegistry singleton(Customizer customizer) {
        CustomizerRegistry registry = new CustomizerRegistry(new Class[]{Customizer.class});
        registry.add(customizer);
        return registry;
    }
}
