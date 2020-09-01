package com.vick.test.util.jdbc.beans;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
@SuppressWarnings("serial")
public class BeanDefinitionParsingException extends BeanDefinitionStoreException {

    /**
     * Create a new BeanDefinitionParsingException.
     * @param problem the configuration problem that was detected during the parsing process
     */
    public BeanDefinitionParsingException(Problem problem) {
        super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
    }

}
