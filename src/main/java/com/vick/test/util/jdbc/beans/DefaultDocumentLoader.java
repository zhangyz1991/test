package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.XmlValidationModeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class DefaultDocumentLoader implements DocumentLoader {

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";


    private static final Logger logger = LoggerFactory.getLogger(DefaultDocumentLoader.class);


    /**
     * Load the {@link Document} at the supplied {@link InputSource} using the standard JAXP-configured
     * XML parser.
     */
    @Override
    public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
                                 ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

        DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
        if (logger.isTraceEnabled()) {
            logger.trace("Using JAXP provider [" + factory.getClass().getName() + "]");
        }
        DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
        return builder.parse(inputSource);
    }

    /**
     * Create the {@link DocumentBuilderFactory} instance.
     * @param validationMode the type of validation: {link XmlValidationModeDetector#VALIDATION_DTD DTD}
     * or {link XmlValidationModeDetector#VALIDATION_XSD XSD})
     * @param namespaceAware whether the returned factory is to provide support for XML namespaces
     * @return the JAXP DocumentBuilderFactory
     * @throws ParserConfigurationException if we failed to build a proper DocumentBuilderFactory
     */
    protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
            throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);

        if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
            factory.setValidating(true);
            if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
                // Enforce namespace aware for XSD...
                factory.setNamespaceAware(true);
                try {
                    factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
                }
                catch (IllegalArgumentException ex) {
                    ParserConfigurationException pcex = new ParserConfigurationException(
                            "Unable to validate using XSD: Your JAXP provider [" + factory +
                                    "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
                                    "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
                    pcex.initCause(ex);
                    throw pcex;
                }
            }
        }

        return factory;
    }

    /**
     * Create a JAXP DocumentBuilder that this bean definition reader
     * will use for parsing XML documents. Can be overridden in subclasses,
     * adding further initialization of the builder.
     * @param factory the JAXP DocumentBuilderFactory that the DocumentBuilder
     * should be created with
     * @param entityResolver the SAX EntityResolver to use
     * @param errorHandler the SAX ErrorHandler to use
     * @return the JAXP DocumentBuilder
     * @throws ParserConfigurationException if thrown by JAXP methods
     */
    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory,
                                                    @Nullable EntityResolver entityResolver, @Nullable ErrorHandler errorHandler)
            throws ParserConfigurationException {

        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        if (entityResolver != null) {
            docBuilder.setEntityResolver(entityResolver);
        }
        if (errorHandler != null) {
            docBuilder.setErrorHandler(errorHandler);
        }
        return docBuilder;
    }

}
