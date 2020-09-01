package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class SpringNamingPolicy extends DefaultNamingPolicy {

    public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();

    @Override
    protected String getTag() {
        return "BySpringCGLIB";
    }

}
