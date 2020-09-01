package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public class ManagedArray extends ManagedList<Object> {

    /** Resolved element type for runtime creation of the target array. */
    @Nullable
    volatile Class<?> resolvedElementType;


    /**
     * Create a new managed array placeholder.
     * @param elementTypeName the target element type as a class name
     * @param size the size of the array
     */
    public ManagedArray(String elementTypeName, int size) {
        super(size);
        Assert.notNull(elementTypeName, "elementTypeName must not be null");
        setElementTypeName(elementTypeName);
    }

}
