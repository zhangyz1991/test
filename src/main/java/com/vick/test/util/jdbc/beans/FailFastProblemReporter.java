package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class FailFastProblemReporter implements ProblemReporter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * Set the {link Log logger} that is to be used to report warnings.
     * <p>If set to {@code null} then a default {link Log logger} set to
     * the name of the instance class will be used.
     * @param logger the {link Log logger} that is to be used to report warnings
     */
    public void setLogger(@Nullable Logger logger) {
        this.logger = (logger != null ? logger : LoggerFactory.getLogger(getClass()));
    }


    /**
     * Throws a {link BeanDefinitionParsingException} detailing the error
     * that has occurred.
     * @param problem the source of the error
     */
    @Override
    public void fatal(Problem problem) {
        throw new BeanDefinitionParsingException(problem);
    }

    /**
     * Throws a {@link BeanDefinitionParsingException} detailing the error
     * that has occurred.
     * @param problem the source of the error
     */
    @Override
    public void error(Problem problem) {
        throw new BeanDefinitionParsingException(problem);
    }

    /**
     * Writes the supplied {@link Problem} to the {link Log} at {@code WARN} level.
     * @param problem the source of the warning
     */
    @Override
    public void warning(Problem problem) {
        logger.warn(problem.toString(), problem.getRootCause());
    }

}
