package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.ClassVisitor;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public abstract class ClassTransformer extends ClassVisitor {
    public ClassTransformer() {
        super(Constants.ASM_API);
    }

    public ClassTransformer(int opcode) {
        super(opcode);
    }

    public abstract void setTarget(ClassVisitor var1);
}
