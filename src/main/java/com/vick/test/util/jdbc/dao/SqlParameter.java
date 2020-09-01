package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;
import com.vick.test.util.jdbc.framework.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public class SqlParameter {
    @Nullable
    private String name;
    private final int sqlType;
    @Nullable
    private String typeName;
    @Nullable
    private Integer scale;

    public SqlParameter(int sqlType) {
        this.sqlType = sqlType;
    }

    public SqlParameter(int sqlType, @Nullable String typeName) {
        this.sqlType = sqlType;
        this.typeName = typeName;
    }

    public SqlParameter(int sqlType, int scale) {
        this.sqlType = sqlType;
        this.scale = scale;
    }

    public SqlParameter(String name, int sqlType) {
        this.name = name;
        this.sqlType = sqlType;
    }

    public SqlParameter(String name, int sqlType, @Nullable String typeName) {
        this.name = name;
        this.sqlType = sqlType;
        this.typeName = typeName;
    }

    public SqlParameter(String name, int sqlType, int scale) {
        this.name = name;
        this.sqlType = sqlType;
        this.scale = scale;
    }

    public SqlParameter(SqlParameter otherParam) {
        Assert.notNull(otherParam, "SqlParameter object must not be null");
        this.name = otherParam.name;
        this.sqlType = otherParam.sqlType;
        this.typeName = otherParam.typeName;
        this.scale = otherParam.scale;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public int getSqlType() {
        return this.sqlType;
    }

    @Nullable
    public String getTypeName() {
        return this.typeName;
    }

    @Nullable
    public Integer getScale() {
        return this.scale;
    }

    public boolean isInputValueProvided() {
        return true;
    }

    public boolean isResultsParameter() {
        return false;
    }

    public static List<SqlParameter> sqlTypesToAnonymousParameterList(@Nullable int... types) {
        if (types == null) {
            return new LinkedList();
        } else {
            List<SqlParameter> result = new ArrayList(types.length);
            int[] var2 = types;
            int var3 = types.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int type = var2[var4];
                result.add(new SqlParameter(type));
            }

            return result;
        }
    }
}

