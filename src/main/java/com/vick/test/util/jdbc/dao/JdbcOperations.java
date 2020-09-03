package com.vick.test.util.jdbc.dao;

import org.springframework.lang.Nullable;

import java.util.List;

public interface JdbcOperations {

    <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

    @Nullable
    <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException;
}
