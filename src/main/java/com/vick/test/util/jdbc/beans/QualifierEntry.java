package com.vick.test.util.jdbc.beans;

import com.vick.test.util.jdbc.framework.StringUtils;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class QualifierEntry implements ParseState.Entry {

    private String typeName;


    public QualifierEntry(String typeName) {
        if (!StringUtils.hasText(typeName)) {
            throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
        }
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "Qualifier '" + this.typeName + "'";
    }

}
