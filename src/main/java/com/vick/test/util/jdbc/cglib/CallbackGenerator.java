package com.vick.test.util.jdbc.cglib;

import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
interface CallbackGenerator {
    void generate(ClassEmitter var1, CallbackGenerator.Context var2, List var3) throws Exception;

    void generateStatic(CodeEmitter var1, CallbackGenerator.Context var2, List var3) throws Exception;

    public interface Context {
        ClassLoader getClassLoader();

        CodeEmitter beginMethod(ClassEmitter var1, MethodInfo var2);

        int getOriginalModifiers(MethodInfo var1);

        int getIndex(MethodInfo var1);

        void emitCallback(CodeEmitter var1, int var2);

        Signature getImplSignature(MethodInfo var1);

        void emitLoadArgsAndInvoke(CodeEmitter var1, MethodInfo var2);
    }
}
