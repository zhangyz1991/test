package com.vick.test.util.jdbc.beans;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class XmlBeanDefinitionStoreException extends BeanDefinitionStoreException {

    /**
     * Create a new XmlBeanDefinitionStoreException.
     * @param resourceDescription description of the resource that the bean definition came from
     * @param msg the detail message (used as exception message as-is)
     * @param cause the SAXException (typically a SAXParseException) root cause
     * @see org.xml.sax.SAXParseException
     */
    public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause) {
        super(resourceDescription, msg, cause);
    }

    /**
     * Return the line number in the XML resource that failed.
     * @return the line number if available (in case of a SAXParseException); -1 else
     * @see org.xml.sax.SAXParseException#getLineNumber()
     */
    public int getLineNumber() {
        Throwable cause = getCause();
        if (cause instanceof SAXParseException) {
            return ((SAXParseException) cause).getLineNumber();
        }
        return -1;
    }

}
