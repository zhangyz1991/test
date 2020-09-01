package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface HashCodeCustomizer extends KeyFactoryCustomizer {
    boolean customize(CodeEmitter var1, Type var2);
}
