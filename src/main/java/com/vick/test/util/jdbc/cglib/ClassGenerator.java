package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.ClassVisitor;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface ClassGenerator {
    void generateClass(ClassVisitor var1) throws Exception;
}
