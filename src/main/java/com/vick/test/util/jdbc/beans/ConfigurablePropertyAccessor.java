package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.convert.ConversionService;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/30
 */
public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {

    /**
     * Specify a Spring 3.0 ConversionService to use for converting
     * property values, as an alternative to JavaBeans PropertyEditors.
     */
    void setConversionService(@Nullable ConversionService conversionService);

    /**
     * Return the associated ConversionService, if any.
     */
    @Nullable
    ConversionService getConversionService();

    /**
     * Set whether to extract the old property value when applying a
     * property editor to a new value for a property.
     */
    void setExtractOldValueForEditor(boolean extractOldValueForEditor);

    /**
     * Return whether to extract the old property value when applying a
     * property editor to a new value for a property.
     */
    boolean isExtractOldValueForEditor();

    /**
     * Set whether this instance should attempt to "auto-grow" a
     * nested path that contains a {@code null} value.
     * <p>If {@code true}, a {@code null} path location will be populated
     * with a default object value and traversed instead of resulting in a
     * {link NullValueInNestedPathException}.
     * <p>Default is {@code false} on a plain PropertyAccessor instance.
     */
    void setAutoGrowNestedPaths(boolean autoGrowNestedPaths);

    /**
     * Return whether "auto-growing" of nested paths has been activated.
     */
    boolean isAutoGrowNestedPaths();

}
