package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public interface BeanExpressionResolver {

    /**
     * Evaluate the given value as an expression, if applicable;
     * return the value as-is otherwise.
     * @param value the value to check
     * @param evalContext the evaluation context
     * @return the resolved value (potentially the given value as-is)
     * @throws BeansException if evaluation failed
     */
    @Nullable
    Object evaluate(@Nullable String value, BeanExpressionContext evalContext) throws BeansException;

}
