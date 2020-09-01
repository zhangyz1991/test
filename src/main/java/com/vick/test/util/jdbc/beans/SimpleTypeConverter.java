package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class SimpleTypeConverter extends TypeConverterSupport {

    public SimpleTypeConverter() {
        this.typeConverterDelegate = new TypeConverterDelegate(this);
        registerDefaultEditors();
    }

}
