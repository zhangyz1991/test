package com.vick.test.util.jdbc.dao;

import com.sun.istack.internal.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlTypeValue {
    int TYPE_UNKNOWN = -2147483648;

    void setTypeValue(PreparedStatement var1, int var2, int var3, @Nullable String var4) throws SQLException;
}
