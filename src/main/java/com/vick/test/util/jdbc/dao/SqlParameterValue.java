package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public class SqlParameterValue extends SqlParameter {
    @Nullable
    private final Object value;

    public SqlParameterValue(int sqlType, @Nullable Object value) {
        super(sqlType);
        this.value = value;
    }

    public SqlParameterValue(int sqlType, @Nullable String typeName, @Nullable Object value) {
        super(sqlType, typeName);
        this.value = value;
    }

    public SqlParameterValue(int sqlType, int scale, @Nullable Object value) {
        super(sqlType, scale);
        this.value = value;
    }

    public SqlParameterValue(SqlParameter declaredParam, @Nullable Object value) {
        super(declaredParam);
        this.value = value;
    }

    @Nullable
    public Object getValue() {
        return this.value;
    }
}

