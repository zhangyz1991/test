package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.Resource;
import com.vick.test.util.jdbc.framework.io.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class ResourceEntityResolver extends DelegatingEntityResolver {

    private static final Logger logger = LoggerFactory.getLogger(ResourceEntityResolver.class);

    private final ResourceLoader resourceLoader;


    /**
     * Create a ResourceEntityResolver for the specified ResourceLoader
     * (usually, an ApplicationContext).
     * @param resourceLoader the ResourceLoader (or ApplicationContext)
     * to load XML entity includes with
     */
    public ResourceEntityResolver(ResourceLoader resourceLoader) {
        super(resourceLoader.getClassLoader());
        this.resourceLoader = resourceLoader;
    }


    @Override
    @Nullable
    public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId)
            throws SAXException, IOException {

        InputSource source = super.resolveEntity(publicId, systemId);

        if (source == null && systemId != null) {
            String resourcePath = null;
            try {
                String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
                String givenUrl = new URL(decodedSystemId).toString();
                String systemRootUrl = new File("").toURI().toURL().toString();
                // Try relative to resource base if currently in system root.
                if (givenUrl.startsWith(systemRootUrl)) {
                    resourcePath = givenUrl.substring(systemRootUrl.length());
                }
            }
            catch (Exception ex) {
                // Typically a MalformedURLException or AccessControlException.
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
                }
                // No URL (or no resolvable URL) -> try relative to resource base.
                resourcePath = systemId;
            }
            if (resourcePath != null) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
                }
                Resource resource = this.resourceLoader.getResource(resourcePath);
                source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found XML entity [" + systemId + "]: " + resource);
                }
            }
            else if (systemId.endsWith(DTD_SUFFIX) || systemId.endsWith(XSD_SUFFIX)) {
                // External dtd/xsd lookup via https even for canonical http declaration
                String url = systemId;
                if (url.startsWith("http:")) {
                    url = "https:" + url.substring(5);
                }
                try {
                    source = new InputSource(new URL(url).openStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                }
                catch (IOException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not resolve XML entity [" + systemId + "] through URL [" + url + "]", ex);
                    }
                    // Fall back to the parser's default behavior.
                    source = null;
                }
            }
        }

        return source;
    }

}
