package com.vick.test.util.jdbc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlValue {
    void setValue(PreparedStatement var1, int var2) throws SQLException;

    void cleanup();
}
