package com.vick.test.reflection;

import java.lang.reflect.Constructor;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class SampleConstructor {
    public static void main(String[] args) {
        A r = new A();
        printConstructors(r);
    }

    private static void printConstructors(A r) {
        Class c = r.getClass();
        String className = c.getName();
        try {
            Constructor[] theConstructors = c.getConstructors();
            for (int i = 0; i < theConstructors.length; i++) {
                Class[] parameterTypes = theConstructors[i].getParameterTypes();
                System.out.println(className + "(");
                for (int i1 = 0; i1 < parameterTypes.length; i1++) {
                    System.out.println(parameterTypes[i1].getName() + " ");
                }
                System.out.println(")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
