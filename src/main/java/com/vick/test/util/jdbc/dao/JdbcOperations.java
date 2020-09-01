package com.vick.test.util.jdbc.dao;

import java.util.List;

public interface JdbcOperations {

    <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;
}
