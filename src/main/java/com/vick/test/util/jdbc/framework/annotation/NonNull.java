package com.vick.test.util.jdbc.framework.annotation;

import com.vick.test.util.jdbc.google.TypeQualifierNickname;
import com.vick.test.util.jdbc.framework.Nonnull;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Nonnull
@TypeQualifierNickname
public @interface NonNull {
}
