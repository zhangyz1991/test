package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.beans.PropertyChangeEvent;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class ConversionNotSupportedException extends TypeMismatchException {

    /**
     * Create a new ConversionNotSupportedException.
     * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
     * @param requiredType the required target type (or {@code null} if not known)
     * @param cause the root cause (may be {@code null})
     */
    public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent,
                                           @Nullable Class<?> requiredType, @Nullable Throwable cause) {
        super(propertyChangeEvent, requiredType, cause);
    }

    /**
     * Create a new ConversionNotSupportedException.
     * @param value the offending value that couldn't be converted (may be {@code null})
     * @param requiredType the required target type (or {@code null} if not known)
     * @param cause the root cause (may be {@code null})
     */
    public ConversionNotSupportedException(@Nullable Object value, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
        super(value, requiredType, cause);
    }

}
