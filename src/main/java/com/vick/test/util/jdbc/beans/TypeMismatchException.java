package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.ClassUtils;
import com.vick.test.util.jdbc.framework.Nullable;

import java.beans.PropertyChangeEvent;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class TypeMismatchException extends PropertyAccessException {

    /**
     * Error code that a type mismatch error will be registered with.
     */
    public static final String ERROR_CODE = "typeMismatch";


    @Nullable
    private String propertyName;

    @Nullable
    private transient Object value;

    @Nullable
    private Class<?> requiredType;


    /**
     * Create a new {@code TypeMismatchException}.
     * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
     * @param requiredType the required target type
     */
    public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType) {
        this(propertyChangeEvent, requiredType, null);
    }

    /**
     * Create a new {@code TypeMismatchException}.
     * @param propertyChangeEvent the PropertyChangeEvent that resulted in the problem
     * @param requiredType the required target type (or {@code null} if not known)
     * @param cause the root cause (may be {@code null})
     */
    public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, @Nullable Class<?> requiredType,
                                 @Nullable Throwable cause) {

        super(propertyChangeEvent,
                "Failed to convert property value of type '" +
                        ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue()) + "'" +
                        (requiredType != null ?
                                " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : "") +
                        (propertyChangeEvent.getPropertyName() != null ?
                                " for property '" + propertyChangeEvent.getPropertyName() + "'" : ""),
                cause);
        this.propertyName = propertyChangeEvent.getPropertyName();
        this.value = propertyChangeEvent.getNewValue();
        this.requiredType = requiredType;
    }

    /**
     * Create a new {@code TypeMismatchException} without a {@code PropertyChangeEvent}.
     * @param value the offending value that couldn't be converted (may be {@code null})
     * @param requiredType the required target type (or {@code null} if not known)
     * @see #initPropertyName
     */
    public TypeMismatchException(@Nullable Object value, @Nullable Class<?> requiredType) {
        this(value, requiredType, null);
    }

    /**
     * Create a new {@code TypeMismatchException} without a {@code PropertyChangeEvent}.
     * @param value the offending value that couldn't be converted (may be {@code null})
     * @param requiredType the required target type (or {@code null} if not known)
     * @param cause the root cause (may be {@code null})
     * @see #initPropertyName
     */
    public TypeMismatchException(@Nullable Object value, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
        super("Failed to convert value of type '" + ClassUtils.getDescriptiveType(value) + "'" +
                        (requiredType != null ? " to required type '" + ClassUtils.getQualifiedName(requiredType) + "'" : ""),
                cause);
        this.value = value;
        this.requiredType = requiredType;
    }


    /**
     * Initialize this exception's property name for exposure through {@link #getPropertyName()},
     * as an alternative to having it initialized via a {@link PropertyChangeEvent}.
     * @param propertyName the property name to expose
     * @since 5.0.4
     * @see #TypeMismatchException(Object, Class)
     * @see #TypeMismatchException(Object, Class, Throwable)
     */
    public void initPropertyName(String propertyName) {
        Assert.state(this.propertyName == null, "Property name already initialized");
        this.propertyName = propertyName;
    }

    /**
     * Return the name of the affected property, if available.
     */
    @Override
    @Nullable
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Return the offending value (may be {@code null}).
     */
    @Override
    @Nullable
    public Object getValue() {
        return this.value;
    }

    /**
     * Return the required target type, if any.
     */
    @Nullable
    public Class<?> getRequiredType() {
        return this.requiredType;
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

}
