package com.vick.test.database;

import com.vick.test.database.entity.Score;
import com.vick.test.util.jdbc.dao.BeanPropertyRowMapper;
import com.vick.test.util.jdbc.dao.CustomDataSource;
import com.vick.test.util.jdbc.dao.JdbcTemplate;
import com.vick.test.util.jdbc.dao.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class DatabaseTest {
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
        String name = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://139.196.165.243:3306/vick_test";
        String user = "root";
        String password = "success";
        DataSource dataSource = new CustomDataSource(name, url, user, password);
        return dataSource;
    }

}
