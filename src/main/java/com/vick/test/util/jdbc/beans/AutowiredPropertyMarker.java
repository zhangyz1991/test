package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.io.Serializable;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public final class AutowiredPropertyMarker implements Serializable {

    /**
     * The canonical instance for the autowired marker value.
     */
    public static final Object INSTANCE = new AutowiredPropertyMarker();


    private AutowiredPropertyMarker() {
    }

    private Object readResolve() {
        return INSTANCE;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return (this == obj);
    }

    @Override
    public int hashCode() {
        return AutowiredPropertyMarker.class.hashCode();
    }

    @Override
    public String toString() {
        return "(autowired)";
    }

}
