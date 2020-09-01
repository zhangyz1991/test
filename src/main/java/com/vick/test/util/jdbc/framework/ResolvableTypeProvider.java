package com.vick.test.util.jdbc.framework;

public interface ResolvableTypeProvider {

    /**
     * Return the {@link ResolvableType} describing this instance
     * (or {@code null} if some sort of default should be applied instead).
     */
    @Nullable
    ResolvableType getResolvableType();

}
