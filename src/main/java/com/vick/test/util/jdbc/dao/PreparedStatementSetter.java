package com.vick.test.util.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {
    void setValues(PreparedStatement var1) throws SQLException;
}
