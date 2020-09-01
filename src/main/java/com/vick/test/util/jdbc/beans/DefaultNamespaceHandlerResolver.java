package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.ClassUtils;
import com.vick.test.util.jdbc.framework.CollectionUtils;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.PropertiesLoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DefaultNamespaceHandlerResolver implements NamespaceHandlerResolver {

    /**
     * The location to look for the mapping files. Can be present in multiple JAR files.
     */
    public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";


    /** Logger available to subclasses. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** ClassLoader to use for NamespaceHandler classes. */
    @Nullable
    private final ClassLoader classLoader;

    /** Resource location to search for. */
    private final String handlerMappingsLocation;

    /** Stores the mappings from namespace URI to NamespaceHandler class name / instance. */
    @Nullable
    private volatile Map<String, Object> handlerMappings;


    /**
     * Create a new {@code DefaultNamespaceHandlerResolver} using the
     * default mapping file location.
     * <p>This constructor will result in the thread context ClassLoader being used
     * to load resources.
     * @see #DEFAULT_HANDLER_MAPPINGS_LOCATION
     */
    public DefaultNamespaceHandlerResolver() {
        this(null, DEFAULT_HANDLER_MAPPINGS_LOCATION);
    }

    /**
     * Create a new {@code DefaultNamespaceHandlerResolver} using the
     * default mapping file location.
     * @param classLoader the {@link ClassLoader} instance used to load mapping resources
     * (may be {@code null}, in which case the thread context ClassLoader will be used)
     * @see #DEFAULT_HANDLER_MAPPINGS_LOCATION
     */
    public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader) {
        this(classLoader, DEFAULT_HANDLER_MAPPINGS_LOCATION);
    }

    /**
     * Create a new {@code DefaultNamespaceHandlerResolver} using the
     * supplied mapping file location.
     * @param classLoader the {@link ClassLoader} instance used to load mapping resources
     * may be {@code null}, in which case the thread context ClassLoader will be used)
     * @param handlerMappingsLocation the mapping file location
     */
    public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader, String handlerMappingsLocation) {
        Assert.notNull(handlerMappingsLocation, "Handler mappings location must not be null");
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
        this.handlerMappingsLocation = handlerMappingsLocation;
    }


    /**
     * Locate the {@link NamespaceHandler} for the supplied namespace URI
     * from the configured mappings.
     * @param namespaceUri the relevant namespace URI
     * @return the located {@link NamespaceHandler}, or {@code null} if none found
     */
    @Override
    @Nullable
    public NamespaceHandler resolve(String namespaceUri) {
        Map<String, Object> handlerMappings = getHandlerMappings();
        Object handlerOrClassName = handlerMappings.get(namespaceUri);
        if (handlerOrClassName == null) {
            return null;
        }
        else if (handlerOrClassName instanceof NamespaceHandler) {
            return (NamespaceHandler) handlerOrClassName;
        }
        else {
            String className = (String) handlerOrClassName;
            try {
                Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
                if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
                    throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri +
                            "] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
                }
                NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils.instantiateClass(handlerClass);
                namespaceHandler.init();
                handlerMappings.put(namespaceUri, namespaceHandler);
                return namespaceHandler;
            }
            catch (ClassNotFoundException ex) {
                throw new FatalBeanException("Could not find NamespaceHandler class [" + className +
                        "] for namespace [" + namespaceUri + "]", ex);
            }
            catch (LinkageError err) {
                throw new FatalBeanException("Unresolvable class definition for NamespaceHandler class [" +
                        className + "] for namespace [" + namespaceUri + "]", err);
            }
        }
    }

    /**
     * Load the specified NamespaceHandler mappings lazily.
     */
    private Map<String, Object> getHandlerMappings() {
        Map<String, Object> handlerMappings = this.handlerMappings;
        if (handlerMappings == null) {
            synchronized (this) {
                handlerMappings = this.handlerMappings;
                if (handlerMappings == null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Loading NamespaceHandler mappings from [" + this.handlerMappingsLocation + "]");
                    }
                    try {
                        Properties mappings =
                                PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
                        if (logger.isTraceEnabled()) {
                            logger.trace("Loaded NamespaceHandler mappings: " + mappings);
                        }
                        handlerMappings = new ConcurrentHashMap<>(mappings.size());
                        CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
                        this.handlerMappings = handlerMappings;
                    }
                    catch (IOException ex) {
                        throw new IllegalStateException(
                                "Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", ex);
                    }
                }
            }
        }
        return handlerMappings;
    }


    @Override
    public String toString() {
        return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
    }

}
