package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Label;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public interface ProcessSwitchCallback {
    void processCase(int var1, Label var2) throws Exception;

    void processDefault() throws Exception;
}
