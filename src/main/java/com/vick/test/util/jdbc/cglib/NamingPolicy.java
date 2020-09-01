package com.vick.test.util.jdbc.cglib;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface NamingPolicy {
    String getClassName(String var1, String var2, Object var3, Predicate var4);

    boolean equals(Object var1);
}
