package com.vick.test.util.jdbc.framework.env;

import java.util.Map;
import java.util.Properties;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class PropertiesPropertySource extends MapPropertySource {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public PropertiesPropertySource(String name, Properties source) {
        super(name, (Map) source);
    }

    protected PropertiesPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }


    @Override
    public String[] getPropertyNames() {
        synchronized (this.source) {
            return super.getPropertyNames();
        }
    }

}
