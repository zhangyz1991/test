package com.vick.test.util.jdbc.propertyeditors;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.env.PropertyResolver;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.env.Environment;
import com.vick.test.util.jdbc.framework.env.StandardEnvironment;
import com.vick.test.util.jdbc.framework.io.PathMatchingResourcePatternResolver;
import com.vick.test.util.jdbc.framework.io.Resource;
import com.vick.test.util.jdbc.framework.io.ResourcePatternResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class ResourceArrayPropertyEditor extends PropertyEditorSupport {

    private static final Logger logger = LoggerFactory.getLogger(ResourceArrayPropertyEditor.class);

    private final ResourcePatternResolver resourcePatternResolver;

    @Nullable
    private PropertyResolver propertyResolver;

    private final boolean ignoreUnresolvablePlaceholders;


    /**
     * Create a new ResourceArrayPropertyEditor with a default
     * {@link PathMatchingResourcePatternResolver} and {@link StandardEnvironment}.
     * @see PathMatchingResourcePatternResolver
     * @see Environment
     */
    public ResourceArrayPropertyEditor() {
        this(new PathMatchingResourcePatternResolver(), null, true);
    }

    /**
     * Create a new ResourceArrayPropertyEditor with the given {@link ResourcePatternResolver}
     * and {@link PropertyResolver} (typically an {@link Environment}).
     * @param resourcePatternResolver the ResourcePatternResolver to use
     * @param propertyResolver the PropertyResolver to use
     */
    public ResourceArrayPropertyEditor(
            ResourcePatternResolver resourcePatternResolver, @Nullable PropertyResolver propertyResolver) {

        this(resourcePatternResolver, propertyResolver, true);
    }

    /**
     * Create a new ResourceArrayPropertyEditor with the given {@link ResourcePatternResolver}
     * and {@link PropertyResolver} (typically an {@link Environment}).
     * @param resourcePatternResolver the ResourcePatternResolver to use
     * @param propertyResolver the PropertyResolver to use
     * @param ignoreUnresolvablePlaceholders whether to ignore unresolvable placeholders
     * if no corresponding system property could be found
     */
    public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver,
                                       @Nullable PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {

        Assert.notNull(resourcePatternResolver, "ResourcePatternResolver must not be null");
        this.resourcePatternResolver = resourcePatternResolver;
        this.propertyResolver = propertyResolver;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    /**
     * Treat the given text as a location pattern and convert it to a Resource array.
     */
    @Override
    public void setAsText(String text) {
        String pattern = resolvePath(text).trim();
        try {
            setValue(this.resourcePatternResolver.getResources(pattern));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException(
                    "Could not resolve resource location pattern [" + pattern + "]: " + ex.getMessage());
        }
    }

    /**
     * Treat the given value as a collection or array and convert it to a Resource array.
     * Considers String elements as location patterns and takes Resource elements as-is.
     */
    @Override
    public void setValue(Object value) throws IllegalArgumentException {
        if (value instanceof Collection || (value instanceof Object[] && !(value instanceof Resource[]))) {
            Collection<?> input = (value instanceof Collection ? (Collection<?>) value : Arrays.asList((Object[]) value));
            List<Resource> merged = new ArrayList<>();
            for (Object element : input) {
                if (element instanceof String) {
                    // A location pattern: resolve it into a Resource array.
                    // Might point to a single resource or to multiple resources.
                    String pattern = resolvePath((String) element).trim();
                    try {
                        Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                        for (Resource resource : resources) {
                            if (!merged.contains(resource)) {
                                merged.add(resource);
                            }
                        }
                    }
                    catch (IOException ex) {
                        // ignore - might be an unresolved placeholder or non-existing base directory
                        if (logger.isDebugEnabled()) {
                            logger.debug("Could not retrieve resources for pattern '" + pattern + "'", ex);
                        }
                    }
                }
                else if (element instanceof Resource) {
                    // A Resource object: add it to the result.
                    Resource resource = (Resource) element;
                    if (!merged.contains(resource)) {
                        merged.add(resource);
                    }
                }
                else {
                    throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" +
                            Resource.class.getName() + "]: only location String and Resource object supported");
                }
            }
            super.setValue(merged.toArray(new Resource[0]));
        }

        else {
            // An arbitrary value: probably a String or a Resource array.
            // setAsText will be called for a String; a Resource array will be used as-is.
            super.setValue(value);
        }
    }

    /**
     * Resolve the given path, replacing placeholders with
     * corresponding system property values if necessary.
     * @param path the original file path
     * @return the resolved file path
     * @see PropertyResolver#resolvePlaceholders
     * @see PropertyResolver#resolveRequiredPlaceholders(String)
     */
    protected String resolvePath(String path) {
        if (this.propertyResolver == null) {
            this.propertyResolver = new StandardEnvironment();
        }
        return (this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) :
                this.propertyResolver.resolveRequiredPlaceholders(path));
    }

}
