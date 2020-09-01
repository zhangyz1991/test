package com.vick.test.reflection;

import java.lang.reflect.Field;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class B {
    public static void main(String[] args) {
        A r = new A();
        Class temp = r.getClass();
        try {
            System.out.println("反射类中所有公有的属情");
            Field[] fb = temp.getFields();
            for (int i = 0; i < fb.length; i++) {
                Class cl = fb[i].getType();
                String name = fb[i].getName();
                System.out.println("name:" + name + " type:" + cl);
            }

            System.out.println("反射类中所有的属性");
            Field[] fa = temp.getDeclaredFields();
            for (int i = 0; i < fa.length; i++) {
                Class cl = fa[i].getType();
                String name = fa[i].getName();
                System.out.println("name:" + name + " type:" + cl);
            }

            System.out.println("反射类中私有属性的值");
            Field f = temp.getDeclaredField("a");
            f.setAccessible(true);
            Integer i = (Integer) f.get(r);
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
