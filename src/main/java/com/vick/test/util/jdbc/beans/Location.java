package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.Resource;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class Location {

    private final Resource resource;

    @Nullable
    private final Object source;


    /**
     * Create a new instance of the {@link Location} class.
     * @param resource the resource with which this location is associated
     */
    public Location(Resource resource) {
        this(resource, null);
    }

    /**
     * Create a new instance of the {@link Location} class.
     * @param resource the resource with which this location is associated
     * @param source the actual location within the associated resource
     * (may be {@code null})
     */
    public Location(Resource resource, @Nullable Object source) {
        Assert.notNull(resource, "Resource must not be null");
        this.resource = resource;
        this.source = source;
    }


    /**
     * Get the resource with which this location is associated.
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * Get the actual location within the associated {@link #getResource() resource}
     * (may be {@code null}).
     * <p>See the {@link Location class level javadoc for this class} for examples
     * of what the actual type of the returned object may be.
     */
    @Nullable
    public Object getSource() {
        return this.source;
    }

}
