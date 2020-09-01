package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public abstract class AbstractPropertyAccessor extends TypeConverterSupport implements ConfigurablePropertyAccessor {

    private boolean extractOldValueForEditor = false;

    private boolean autoGrowNestedPaths = false;


    @Override
    public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
        this.extractOldValueForEditor = extractOldValueForEditor;
    }

    @Override
    public boolean isExtractOldValueForEditor() {
        return this.extractOldValueForEditor;
    }

    @Override
    public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
        this.autoGrowNestedPaths = autoGrowNestedPaths;
    }

    @Override
    public boolean isAutoGrowNestedPaths() {
        return this.autoGrowNestedPaths;
    }


    @Override
    public void setPropertyValue(PropertyValue pv) throws BeansException {
        setPropertyValue(pv.getName(), pv.getValue());
    }

    @Override
    public void setPropertyValues(Map<?, ?> map) throws BeansException {
        setPropertyValues(new MutablePropertyValues(map));
    }

    @Override
    public void setPropertyValues(PropertyValues pvs) throws BeansException {
        setPropertyValues(pvs, false, false);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown) throws BeansException {
        setPropertyValues(pvs, ignoreUnknown, false);
    }

    @Override
    public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid)
            throws BeansException {

        List<PropertyAccessException> propertyAccessExceptions = null;
        List<PropertyValue> propertyValues = (pvs instanceof MutablePropertyValues ?
                ((MutablePropertyValues) pvs).getPropertyValueList() : Arrays.asList(pvs.getPropertyValues()));
        for (PropertyValue pv : propertyValues) {
            try {
                // This method may throw any BeansException, which won't be caught
                // here, if there is a critical failure such as no matching field.
                // We can attempt to deal only with less serious exceptions.
                setPropertyValue(pv);
            }
            catch (NotWritablePropertyException ex) {
                if (!ignoreUnknown) {
                    throw ex;
                }
                // Otherwise, just ignore it and continue...
            }
            catch (NullValueInNestedPathException ex) {
                if (!ignoreInvalid) {
                    throw ex;
                }
                // Otherwise, just ignore it and continue...
            }
            catch (PropertyAccessException ex) {
                if (propertyAccessExceptions == null) {
                    propertyAccessExceptions = new ArrayList<>();
                }
                propertyAccessExceptions.add(ex);
            }
        }

        // If we encountered individual exceptions, throw the composite exception.
        if (propertyAccessExceptions != null) {
            PropertyAccessException[] paeArray = propertyAccessExceptions.toArray(new PropertyAccessException[0]);
            throw new PropertyBatchUpdateException(paeArray);
        }
    }


    // Redefined with public visibility.
    @Override
    @Nullable
    public Class<?> getPropertyType(String propertyPath) {
        return null;
    }

    /**
     * Actually get the value of a property.
     * @param propertyName name of the property to get the value of
     * @return the value of the property
     * @throws InvalidPropertyException if there is no such property or
     * if the property isn't readable
     * @throws PropertyAccessException if the property was valid but the
     * accessor method failed
     */
    @Override
    @Nullable
    public abstract Object getPropertyValue(String propertyName) throws BeansException;

    /**
     * Actually set a property value.
     * @param propertyName name of the property to set value of
     * @param value the new value
     * @throws InvalidPropertyException if there is no such property or
     * if the property isn't writable
     * @throws PropertyAccessException if the property was valid but the
     * accessor method failed or a type mismatch occurred
     */
    @Override
    public abstract void setPropertyValue(String propertyName, @Nullable Object value) throws BeansException;

}
