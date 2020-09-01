package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.io.ClassPathResource;
import com.vick.test.util.jdbc.framework.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class BeansDtdResolver implements EntityResolver {

    private static final String DTD_EXTENSION = ".dtd";

    private static final String DTD_NAME = "spring-beans";

    private static final Logger logger = LoggerFactory.getLogger(BeansDtdResolver.class);


    @Override
    @Nullable
    public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public ID [" + publicId +
                    "] and system ID [" + systemId + "]");
        }

        if (systemId != null && systemId.endsWith(DTD_EXTENSION)) {
            int lastPathSeparator = systemId.lastIndexOf('/');
            int dtdNameStart = systemId.indexOf(DTD_NAME, lastPathSeparator);
            if (dtdNameStart != -1) {
                String dtdFile = DTD_NAME + DTD_EXTENSION;
                if (logger.isTraceEnabled()) {
                    logger.trace("Trying to locate [" + dtdFile + "] in Spring jar on classpath");
                }
                try {
                    Resource resource = new ClassPathResource(dtdFile, getClass());
                    InputSource source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
                    }
                    return source;
                }
                catch (FileNotFoundException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
                    }
                }
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }


    @Override
    public String toString() {
        return "EntityResolver for spring-beans DTD";
    }

}
