package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.StringUtils;
import com.vick.test.util.jdbc.framework.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class CustomSQLErrorCodesTranslation {

    private String[] errorCodes = new String[0];

    @Nullable
    private Class<?> exceptionClass;


    /**
     * Set the SQL error codes to match.
     */
    public void setErrorCodes(String... errorCodes) {
        this.errorCodes = StringUtils.sortStringArray(errorCodes);
    }

    /**
     * Return the SQL error codes to match.
     */
    public String[] getErrorCodes() {
        return this.errorCodes;
    }

    /**
     * Set the exception class for the specified error codes.
     */
    public void setExceptionClass(@Nullable Class<?> exceptionClass) {
        if (exceptionClass != null && !DataAccessException.class.isAssignableFrom(exceptionClass)) {
            throw new IllegalArgumentException("Invalid exception class [" + exceptionClass +
                    "]: needs to be a subclass of [org.springframework.dao.DataAccessException]");
        }
        this.exceptionClass = exceptionClass;
    }

    /**
     * Return the exception class for the specified error codes.
     */
    @Nullable
    public Class<?> getExceptionClass() {
        return this.exceptionClass;
    }

}
