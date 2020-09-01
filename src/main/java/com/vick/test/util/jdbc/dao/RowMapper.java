package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    @Nullable
    T mapRow(ResultSet var1, int var2) throws SQLException;
}
