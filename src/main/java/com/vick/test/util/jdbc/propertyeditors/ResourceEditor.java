package com.vick.test.util.jdbc.propertyeditors;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.StringUtils;
import com.vick.test.util.jdbc.framework.env.PropertyResolver;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.DefaultResourceLoader;
import com.vick.test.util.jdbc.framework.io.ResourceLoader;
import com.vick.test.util.jdbc.framework.env.StandardEnvironment;
import com.vick.test.util.jdbc.framework.io.Resource;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class ResourceEditor extends PropertyEditorSupport {

    private final ResourceLoader resourceLoader;

    @Nullable
    private PropertyResolver propertyResolver;

    private final boolean ignoreUnresolvablePlaceholders;


    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using a {@link DefaultResourceLoader} and {@link StandardEnvironment}.
     */
    public ResourceEditor() {
        this(new DefaultResourceLoader(), null);
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader} and {@link PropertyResolver}.
     * @param resourceLoader the {@code ResourceLoader} to use
     * @param propertyResolver the {@code PropertyResolver} to use
     */
    public ResourceEditor(ResourceLoader resourceLoader, @Nullable PropertyResolver propertyResolver) {
        this(resourceLoader, propertyResolver, true);
    }

    /**
     * Create a new instance of the {@link ResourceEditor} class
     * using the given {@link ResourceLoader}.
     * @param resourceLoader the {@code ResourceLoader} to use
     * @param propertyResolver the {@code PropertyResolver} to use
     * @param ignoreUnresolvablePlaceholders whether to ignore unresolvable placeholders
     * if no corresponding property could be found in the given {@code propertyResolver}
     */
    public ResourceEditor(ResourceLoader resourceLoader, @Nullable PropertyResolver propertyResolver,
                          boolean ignoreUnresolvablePlaceholders) {

        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.resourceLoader = resourceLoader;
        this.propertyResolver = propertyResolver;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    @Override
    public void setAsText(String text) {
        if (StringUtils.hasText(text)) {
            String locationToUse = resolvePath(text).trim();
            setValue(this.resourceLoader.getResource(locationToUse));
        }
        else {
            setValue(null);
        }
    }

    /**
     * Resolve the given path, replacing placeholders with corresponding
     * property values from the {@code environment} if necessary.
     * @param path the original file path
     * @return the resolved file path
     * @see PropertyResolver#resolvePlaceholders
     * @see PropertyResolver#resolveRequiredPlaceholders
     */
    protected String resolvePath(String path) {
        if (this.propertyResolver == null) {
            this.propertyResolver = new StandardEnvironment();
        }
        return (this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) :
                this.propertyResolver.resolveRequiredPlaceholders(path));
    }


    @Override
    @Nullable
    public String getAsText() {
        Resource value = (Resource) getValue();
        try {
            // Try to determine URL for resource.
            return (value != null ? value.getURL().toExternalForm() : "");
        }
        catch (IOException ex) {
            // Couldn't determine resource URL - return null to indicate
            // that there is no appropriate text representation.
            return null;
        }
    }

}

