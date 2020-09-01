package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
    @Nullable
    T doInPreparedStatement(PreparedStatement var1) throws SQLException, DataAccessException;
}
