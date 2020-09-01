package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public class ArgumentPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {
    @Nullable
    private final Object[] args;

    public ArgumentPreparedStatementSetter(@Nullable Object[] args) {
        this.args = args;
    }

    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for(int i = 0; i < this.args.length; ++i) {
                Object arg = this.args[i];
                this.doSetValue(ps, i + 1, arg);
            }
        }

    }

    protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof SqlParameterValue) {
            SqlParameterValue paramValue = (SqlParameterValue)argValue;
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
        } else {
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, -2147483648, argValue);
        }

    }

    public void cleanupParameters() {
        StatementCreatorUtils.cleanupParameters(this.args);
    }
}

