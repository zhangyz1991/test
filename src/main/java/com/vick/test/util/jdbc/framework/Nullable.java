package com.vick.test.util.jdbc.framework;


import com.vick.test.util.jdbc.google.TypeQualifierNickname;
import com.vick.test.util.jdbc.google.When;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull(
        when = When.MAYBE
)
@TypeQualifierNickname
public @interface Nullable {
}
