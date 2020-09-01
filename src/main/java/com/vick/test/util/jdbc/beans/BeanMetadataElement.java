package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

public interface BeanMetadataElement {

    /**
     * Return the configuration source {@code Object} for this metadata element
     * (may be {@code null}).
     */
    @Nullable
    default Object getSource() {
        return null;
    }

}
