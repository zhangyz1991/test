package com.vick.test.database;

import com.vick.test.database.entity.Score;
import com.vick.test.util.jdbc.dao.*;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class Main {
    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DataSource dataSource = getDataSource();
        jdbcTemplate.setDataSource(dataSource);
        String sql = "select * from tb_score";
        RowMapper rowMapper = new BeanPropertyRowMapper<>(Score.class);
        List<Score> resultList = jdbcTemplate.query(sql, null, rowMapper);
        resultList.forEach(result -> System.out.println(result.getId()));
    }

    public static DataSource getDataSource() {
        DataSource dataSource = new CustomDataSource();
        return dataSource;
    }

}
