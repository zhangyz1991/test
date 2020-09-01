package com.vick.test.util.jdbc.beans;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.convert.TypeDescriptor;
import com.vick.test.util.jdbc.framework.ReflectionUtils;
import com.vick.test.util.jdbc.framework.ResolvableType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DirectFieldAccessor extends AbstractNestablePropertyAccessor {

    private final Map<String, FieldPropertyHandler> fieldMap = new HashMap<>();


    /**
     * Create a new DirectFieldAccessor for the given object.
     * @param object object wrapped by this DirectFieldAccessor
     */
    public DirectFieldAccessor(Object object) {
        super(object);
    }

    /**
     * Create a new DirectFieldAccessor for the given object,
     * registering a nested path that the object is in.
     * @param object object wrapped by this DirectFieldAccessor
     * @param nestedPath the nested path of the object
     * @param parent the containing DirectFieldAccessor (must not be {@code null})
     */
    protected DirectFieldAccessor(Object object, String nestedPath, DirectFieldAccessor parent) {
        super(object, nestedPath, parent);
    }


    @Override
    @Nullable
    protected FieldPropertyHandler getLocalPropertyHandler(String propertyName) {
        FieldPropertyHandler propertyHandler = this.fieldMap.get(propertyName);
        if (propertyHandler == null) {
            Field field = ReflectionUtils.findField(getWrappedClass(), propertyName);
            if (field != null) {
                propertyHandler = new FieldPropertyHandler(field);
                this.fieldMap.put(propertyName, propertyHandler);
            }
        }
        return propertyHandler;
    }

    @Override
    protected DirectFieldAccessor newNestedPropertyAccessor(Object object, String nestedPath) {
        return new DirectFieldAccessor(object, nestedPath, this);
    }

    @Override
    protected NotWritablePropertyException createNotWritablePropertyException(String propertyName) {
        PropertyMatches matches = PropertyMatches.forField(propertyName, getRootClass());
        throw new NotWritablePropertyException(
                getRootClass(), getNestedPath() + propertyName,
                matches.buildErrorMessage(), matches.getPossibleMatches());
    }


    private class FieldPropertyHandler extends PropertyHandler {

        private final Field field;

        public FieldPropertyHandler(Field field) {
            super(field.getType(), true, true);
            this.field = field;
        }

        @Override
        public TypeDescriptor toTypeDescriptor() {
            return new TypeDescriptor(this.field);
        }

        @Override
        public ResolvableType getResolvableType() {
            return ResolvableType.forField(this.field);
        }

        @Override
        @Nullable
        public TypeDescriptor nested(int level) {
            return TypeDescriptor.nested(this.field, level);
        }

        @Override
        @Nullable
        public Object getValue() throws Exception {
            try {
                ReflectionUtils.makeAccessible(this.field);
                return this.field.get(getWrappedInstance());
            }

            catch (IllegalAccessException ex) {
                throw new InvalidPropertyException(getWrappedClass(),
                        this.field.getName(), "Field is not accessible", ex);
            }
        }

        @Override
        public void setValue(@Nullable Object value) throws Exception {
            try {
                ReflectionUtils.makeAccessible(this.field);
                this.field.set(getWrappedInstance(), value);
            }
            catch (IllegalAccessException ex) {
                throw new InvalidPropertyException(getWrappedClass(), this.field.getName(),
                        "Field is not accessible", ex);
            }
        }
    }

}
