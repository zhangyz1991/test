package com.vick.test.util.jdbc.google;

import com.vick.test.util.jdbc.framework.Nonnull;

import java.lang.annotation.Annotation;

public interface TypeQualifierValidator<A extends Annotation> {
    @Nonnull
    When forConstantValue(@Nonnull A var1, Object var2);
}
