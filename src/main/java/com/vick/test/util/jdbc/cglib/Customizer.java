package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

public interface Customizer extends KeyFactoryCustomizer {
    void customize(CodeEmitter var1, Type var2);
}
