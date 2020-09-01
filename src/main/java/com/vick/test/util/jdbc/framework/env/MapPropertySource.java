package com.vick.test.util.jdbc.framework.env;

import com.vick.test.util.jdbc.framework.StringUtils;
import com.vick.test.util.jdbc.framework.Nullable;

import java.util.Map;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class MapPropertySource extends EnumerablePropertySource<Map<String, Object>> {

    public MapPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }


    @Override
    @Nullable
    public Object getProperty(String name) {
        return this.source.get(name);
    }

    @Override
    public boolean containsProperty(String name) {
        return this.source.containsKey(name);
    }

    @Override
    public String[] getPropertyNames() {
        return StringUtils.toStringArray(this.source.keySet());
    }

}
