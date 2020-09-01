package com.vick.test.util.jdbc.dao;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseMetaDataCallback {
    Object processMetaData(DatabaseMetaData var1) throws SQLException, MetaDataAccessException;
}

