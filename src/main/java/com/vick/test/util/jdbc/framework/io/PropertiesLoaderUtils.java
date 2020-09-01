package com.vick.test.util.jdbc.framework.io;

import com.vick.test.util.jdbc.framework.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public abstract class PropertiesLoaderUtils {

    private static final String XML_FILE_EXTENSION = ".xml";


    /**
     * Load properties from the given EncodedResource,
     * potentially defining a specific encoding for the properties file.
     * @see #fillProperties(java.util.Properties, EncodedResource)
     */
    public static Properties loadProperties(EncodedResource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    /**
     * Fill the given properties from the given EncodedResource,
     * potentially defining a specific encoding for the properties file.
     * @param props the Properties instance to load into
     * @param resource the resource to load from
     * @throws IOException in case of I/O errors
     */
    public static void fillProperties(Properties props, EncodedResource resource)
            throws IOException {

        fillProperties(props, resource, new DefaultPropertiesPersister());
    }

    /**
     * Actually load properties from the given EncodedResource into the given Properties instance.
     * @param props the Properties instance to load into
     * @param resource the resource to load from
     * @param persister the PropertiesPersister to use
     * @throws IOException in case of I/O errors
     */
    static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister)
            throws IOException {

        InputStream stream = null;
        Reader reader = null;
        try {
            String filename = resource.getResource().getFilename();
            if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                stream = resource.getInputStream();
                persister.loadFromXml(props, stream);
            }
            else if (resource.requiresReader()) {
                reader = resource.getReader();
                persister.load(props, reader);
            }
            else {
                stream = resource.getInputStream();
                persister.load(props, stream);
            }
        }
        finally {
            if (stream != null) {
                stream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Load properties from the given resource (in ISO-8859-1 encoding).
     * @param resource the resource to load from
     * @return the populated Properties instance
     * @throws IOException if loading failed
     * @see #fillProperties(java.util.Properties, Resource)
     */
    public static Properties loadProperties(Resource resource) throws IOException {
        Properties props = new Properties();
        fillProperties(props, resource);
        return props;
    }

    /**
     * Fill the given properties from the given resource (in ISO-8859-1 encoding).
     * @param props the Properties instance to fill
     * @param resource the resource to load from
     * @throws IOException if loading failed
     */
    public static void fillProperties(Properties props, Resource resource) throws IOException {
        InputStream is = resource.getInputStream();
        try {
            String filename = resource.getFilename();
            if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                props.loadFromXML(is);
            }
            else {
                props.load(is);
            }
        }
        finally {
            is.close();
        }
    }

    /**
     * Load all properties from the specified class path resource
     * (in ISO-8859-1 encoding), using the default class loader.
     * <p>Merges properties if more than one resource of the same name
     * found in the class path.
     * @param resourceName the name of the class path resource
     * @return the populated Properties instance
     * @throws IOException if loading failed
     */
    public static Properties loadAllProperties(String resourceName) throws IOException {
        return loadAllProperties(resourceName, null);
    }

    /**
     * Load all properties from the specified class path resource
     * (in ISO-8859-1 encoding), using the given class loader.
     * <p>Merges properties if more than one resource of the same name
     * found in the class path.
     * @param resourceName the name of the class path resource
     * @param classLoader the ClassLoader to use for loading
     * (or {@code null} to use the default class loader)
     * @return the populated Properties instance
     * @throws IOException if loading failed
     */
    public static Properties loadAllProperties(String resourceName, @Nullable ClassLoader classLoader) throws IOException {
        Assert.notNull(resourceName, "Resource name must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = ClassUtils.getDefaultClassLoader();
        }
        Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(resourceName) :
                ClassLoader.getSystemResources(resourceName));
        Properties props = new Properties();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            URLConnection con = url.openConnection();
            ResourceUtils.useCachesIfNecessary(con);
            InputStream is = con.getInputStream();
            try {
                if (resourceName.endsWith(XML_FILE_EXTENSION)) {
                    props.loadFromXML(is);
                }
                else {
                    props.load(is);
                }
            }
            finally {
                is.close();
            }
        }
        return props;
    }

}
