package com.vick.test.reflection;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class SampleInterface {
    public static void main(String[] args) {
        A raf = new A();
        printInterfaceNames(raf);
    }

    private static void printInterfaceNames(Object o) {
        Class c = o.getClass();
        Class[] theInterfaces = c.getInterfaces();
        for (int i = 0; i < theInterfaces.length; i++) {
            System.out.println(theInterfaces[i].getName());
        }
        Class theSuperClass = c.getSuperclass();
        System.out.println("superClass: " + theSuperClass.getName());
    }
}
