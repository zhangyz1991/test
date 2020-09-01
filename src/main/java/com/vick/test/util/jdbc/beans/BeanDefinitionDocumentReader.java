package com.vick.test.util.jdbc.beans;

import org.w3c.dom.Document;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public interface BeanDefinitionDocumentReader {

    /**
     * Read bean definitions from the given DOM document and
     * register them with the registry in the given reader context.
     * @param doc the DOM document
     * @param readerContext the current context of the reader
     * (includes the target registry and the resource being parsed)
     * @throws BeanDefinitionStoreException in case of parsing errors
     */
    void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
            throws BeanDefinitionStoreException;

}
