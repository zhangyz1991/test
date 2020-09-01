package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.CollectionUtils;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.ClassPathResource;
import com.vick.test.util.jdbc.framework.io.PropertiesLoaderUtils;
import com.vick.test.util.jdbc.framework.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class PluggableSchemaResolver implements EntityResolver {

    /**
     * The location of the file that defines schema mappings.
     * Can be present in multiple JAR files.
     */
    public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";


    private static final Logger logger = LoggerFactory.getLogger(PluggableSchemaResolver.class);

    @Nullable
    private final ClassLoader classLoader;

    private final String schemaMappingsLocation;

    /** Stores the mapping of schema URL -> local schema path. */
    @Nullable
    private volatile Map<String, String> schemaMappings;


    /**
     * Loads the schema URL -> schema file location mappings using the default
     * mapping file pattern "META-INF/spring.schemas".
     * @param classLoader the ClassLoader to use for loading
     * (can be {@code null}) to use the default ClassLoader)
     * see PropertiesLoaderUtils#loadAllProperties(String, ClassLoader)
     */
    public PluggableSchemaResolver(@Nullable ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
    }

    /**
     * Loads the schema URL -> schema file location mappings using the given
     * mapping file pattern.
     * @param classLoader the ClassLoader to use for loading
     * (can be {@code null}) to use the default ClassLoader)
     * @param schemaMappingsLocation the location of the file that defines schema mappings
     * (must not be empty)
     * see PropertiesLoaderUtils#loadAllProperties(String, ClassLoader)
     */
    public PluggableSchemaResolver(@Nullable ClassLoader classLoader, String schemaMappingsLocation) {
        Assert.hasText(schemaMappingsLocation, "'schemaMappingsLocation' must not be empty");
        this.classLoader = classLoader;
        this.schemaMappingsLocation = schemaMappingsLocation;
    }


    @Override
    @Nullable
    public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public id [" + publicId +
                    "] and system id [" + systemId + "]");
        }

        if (systemId != null) {
            String resourceLocation = getSchemaMappings().get(systemId);
            if (resourceLocation == null && systemId.startsWith("https:")) {
                // Retrieve canonical http schema mapping even for https declaration
                resourceLocation = getSchemaMappings().get("http:" + systemId.substring(6));
            }
            if (resourceLocation != null) {
                Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
                try {
                    InputSource source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
                    }
                    return source;
                }
                catch (FileNotFoundException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not find XML schema [" + systemId + "]: " + resource, ex);
                    }
                }
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }

    /**
     * Load the specified schema mappings lazily.
     */
    private Map<String, String> getSchemaMappings() {
        Map<String, String> schemaMappings = this.schemaMappings;
        if (schemaMappings == null) {
            synchronized (this) {
                schemaMappings = this.schemaMappings;
                if (schemaMappings == null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
                    }
                    try {
                        Properties mappings =
                                PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
                        if (logger.isTraceEnabled()) {
                            logger.trace("Loaded schema mappings: " + mappings);
                        }
                        schemaMappings = new ConcurrentHashMap<>(mappings.size());
                        CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
                        this.schemaMappings = schemaMappings;
                    }
                    catch (IOException ex) {
                        throw new IllegalStateException(
                                "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
                    }
                }
            }
        }
        return schemaMappings;
    }


    @Override
    public String toString() {
        return "EntityResolver using schema mappings " + getSchemaMappings();
    }

}
