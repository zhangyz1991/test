package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Nullable;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLExceptionTranslator {
    @Nullable
    DataAccessException translate(String var1, @Nullable String var2, SQLException var3);
}
