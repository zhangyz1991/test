package com.vick.test.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Vick Zhang
 * @create 2020/8/27
 */
public class LoadMethod {

    public static void main(String[] args) {
        LoadMethod loadMethod = new LoadMethod();
        String[] types = {"int", "String"};
        String[] params = {"1", "xxx"};
        Object abc = loadMethod.load(A.class.getName(), "abc", types, params);
        System.out.println(abc);
    }

    public Object load(String cName, String methodName, String[] types, String[] params) {
        Object retObj = null;
        try {
            Class cls = Class.forName(cName);
            //获取指定对象的实例
            Constructor ct = cls.getConstructor(null);
            Object obj = ct.newInstance(null);
            //构建方法参数的数据类型
            Class parTypes[] = this.getMethodClass(types);
            //在指定类中获取指定的方法
            Method method = cls.getMethod(methodName, parTypes);
            //构建方法的参数值
            Object arglist[] = this.getMethodObject(parTypes, params);
            //调用指定的方法并获取返回值为Object类型
            retObj = method.invoke(obj, arglist);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return retObj;
    }

    private Class[] getMethodClass(String[] types) {
        Class[] cs = new Class[types.length];
        for (int i = 0; i < cs.length; i++) {
            if (types[i] != null && !types[i].equals("")) {
                if (types[i].equals("int") || types[i].equals("Integer")) {
                    cs[i] = Integer.TYPE;
                } else if (types[i].equals("float") || types[i].equals("Float")) {
                    cs[i] = Float.TYPE;
                } else if (types[i].equals("double") || types[i].equals("Double")) {
                    cs[i] = Double.TYPE;
                } else if (types[i].equals("boolean") || types[i].equals("Boolean")) {
                    cs[i] = Boolean.TYPE;
                } else {
                    cs[i] = String.class;
                }
            }
        }
        return cs;
    }

    private Object[] getMethodObject(Class[] parTypes, String[] params) {
        Object[] objs = new Object[params.length];
        for (int i = 0; i < objs.length; i++) {
            if (parTypes[i].equals(Integer.TYPE)) {
                objs[i] = new Integer(params[i]);
            } else if (parTypes[i].equals(Float.TYPE)) {
                objs[i] = new Float(params[i]);
            } else if (parTypes[i].equals(Double.TYPE)) {
                objs[i] = new Double(params[i]);
            } else if (parTypes[i].equals(Boolean.TYPE)) {
                objs[i] = new Boolean(params[i]);
            } else {
                objs[i] = params[i];
            }
        }
        return objs;
    }

}
