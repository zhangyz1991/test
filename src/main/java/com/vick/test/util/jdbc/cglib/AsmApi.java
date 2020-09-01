package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Opcodes;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
final class AsmApi {

    /**
     * SPRING PATCH: always returns ASM7.
     */
    static int value() {
        return Opcodes.ASM7;
    }

    private AsmApi() {
    }

}

