package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface FieldTypeCustomizer extends KeyFactoryCustomizer {
    void customize(CodeEmitter var1, int var2, Type var3);

    Type getOutType(int var1, Type var2);
}
