package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
@SuppressWarnings("serial")
public class FactoryBeanNotInitializedException extends FatalBeanException {

    /**
     * Create a new FactoryBeanNotInitializedException with the default message.
     */
    public FactoryBeanNotInitializedException() {
        super("FactoryBean is not fully initialized yet");
    }

    /**
     * Create a new FactoryBeanNotInitializedException with the given message.
     * @param msg the detail message
     */
    public FactoryBeanNotInitializedException(String msg) {
        super(msg);
    }

}
