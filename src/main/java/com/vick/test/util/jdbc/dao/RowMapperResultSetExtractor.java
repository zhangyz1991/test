package com.vick.test.util.jdbc.dao;

import com.vick.test.util.jdbc.framework.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/22
 */
public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
    private final RowMapper<T> rowMapper;
    private final int rowsExpected;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> results = this.rowsExpected > 0 ? new ArrayList(this.rowsExpected) : new ArrayList();
        int var3 = 0;

        while(rs.next()) {
            results.add(this.rowMapper.mapRow(rs, var3++));
        }

        return results;
    }
}

