package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Label;

public interface ObjectSwitchCallback {
    void processCase(Object var1, Label var2) throws Exception;

    void processDefault() throws Exception;
}
