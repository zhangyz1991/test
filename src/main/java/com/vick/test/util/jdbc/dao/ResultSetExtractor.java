package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T> {
    @Nullable
    T extractData(ResultSet var1) throws SQLException, DataAccessException;
}
