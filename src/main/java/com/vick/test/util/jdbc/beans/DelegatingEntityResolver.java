package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.Nullable;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DelegatingEntityResolver implements EntityResolver {

    /** Suffix for DTD files. */
    public static final String DTD_SUFFIX = ".dtd";

    /** Suffix for schema definition files. */
    public static final String XSD_SUFFIX = ".xsd";


    private final EntityResolver dtdResolver;

    private final EntityResolver schemaResolver;


    /**
     * Create a new DelegatingEntityResolver that delegates to
     * a default {@link BeansDtdResolver} and a default {@link PluggableSchemaResolver}.
     * <p>Configures the {@link PluggableSchemaResolver} with the supplied
     * {@link ClassLoader}.
     * @param classLoader the ClassLoader to use for loading
     * (can be {@code null}) to use the default ClassLoader)
     */
    public DelegatingEntityResolver(@Nullable ClassLoader classLoader) {
        this.dtdResolver = new BeansDtdResolver();
        this.schemaResolver = new PluggableSchemaResolver(classLoader);
    }

    /**
     * Create a new DelegatingEntityResolver that delegates to
     * the given {@link EntityResolver EntityResolvers}.
     * @param dtdResolver the EntityResolver to resolve DTDs with
     * @param schemaResolver the EntityResolver to resolve XML schemas with
     */
    public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver) {
        Assert.notNull(dtdResolver, "'dtdResolver' is required");
        Assert.notNull(schemaResolver, "'schemaResolver' is required");
        this.dtdResolver = dtdResolver;
        this.schemaResolver = schemaResolver;
    }


    @Override
    @Nullable
    public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId)
            throws SAXException, IOException {

        if (systemId != null) {
            if (systemId.endsWith(DTD_SUFFIX)) {
                return this.dtdResolver.resolveEntity(publicId, systemId);
            }
            else if (systemId.endsWith(XSD_SUFFIX)) {
                return this.schemaResolver.resolveEntity(publicId, systemId);
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }


    @Override
    public String toString() {
        return "EntityResolver delegating " + XSD_SUFFIX + " to " + this.schemaResolver +
                " and " + DTD_SUFFIX + " to " + this.dtdResolver;
    }

}
