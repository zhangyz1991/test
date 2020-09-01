package com.vick.test.util.jdbc.cglib;

import java.lang.reflect.Method;

public interface CallbackFilter {
    int accept(Method var1);

    boolean equals(Object var1);
}
