package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Type;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class Local {
    private Type type;
    private int index;

    public Local(int index, Type type) {
        this.type = type;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public Type getType() {
        return this.type;
    }
}
