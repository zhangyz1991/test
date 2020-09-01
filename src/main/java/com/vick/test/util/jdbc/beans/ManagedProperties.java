package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

import java.util.Properties;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
@SuppressWarnings("serial")
public class ManagedProperties extends Properties implements Mergeable, BeanMetadataElement {

    @Nullable
    private Object source;

    private boolean mergeEnabled;


    /**
     * Set the configuration source {@code Object} for this metadata element.
     * <p>The exact type of the object will depend on the configuration mechanism used.
     */
    public void setSource(@Nullable Object source) {
        this.source = source;
    }

    @Override
    @Nullable
    public Object getSource() {
        return this.source;
    }

    /**
     * Set whether merging should be enabled for this collection,
     * in case of a 'parent' collection value being present.
     */
    public void setMergeEnabled(boolean mergeEnabled) {
        this.mergeEnabled = mergeEnabled;
    }

    @Override
    public boolean isMergeEnabled() {
        return this.mergeEnabled;
    }


    @Override
    public Object merge(@Nullable Object parent) {
        if (!this.mergeEnabled) {
            throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
        }
        if (parent == null) {
            return this;
        }
        if (!(parent instanceof Properties)) {
            throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
        }
        Properties merged = new ManagedProperties();
        merged.putAll((Properties) parent);
        merged.putAll(this);
        return merged;
    }

}
