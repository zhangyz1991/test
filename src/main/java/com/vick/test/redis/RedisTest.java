package com.vick.test.redis;

import com.vick.test.redis.entity.District;
import com.vick.test.util.jdbc.dao.BeanPropertyRowMapper;
import com.vick.test.util.jdbc.dao.CustomDataSource;
import com.vick.test.util.jdbc.dao.JdbcTemplate;
import com.vick.test.util.jdbc.dao.RowMapper;
import com.vick.test.util.redis.BaseRedisDao;
import com.vick.test.util.redis.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vick Zhang
 * @create 2020/9/1
 */
public class RedisTest {

    /**
     * 地区服务省名和省ID Map key
     */
    public static final String DISTRICT_PROVINCE_NAME_ID_MAP_KEY = "district:province:name:id";

    public static void main(String[] args) {
        //Jedis redis = RedisUtil.getConnect("139.196.165.243", 6379, "success", 2);
        BaseRedisDao baseRedisDao = RedisUtil.getBaseRedisDao("139.196.165.243", 6379, "success", 2);
        //setTest(baseRedisDao);
        getTest(baseRedisDao);

        //RedisTemplate redisTemplate = RedisUtil.getRedisTemplate("139.196.165.243", 6379, "success", 2);
        //setTest(redisTemplate);
    }

    private static void getTest(Jedis redis) {
        //获取map
        try {
            Map<String, Integer> hmget = (Map<String, Integer>) RedisUtil.get(redis, DISTRICT_PROVINCE_NAME_ID_MAP_KEY);
            System.out.println("RedisUtil.set 结果: " + hmget);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setTest(Jedis redis) {
        Map<String, Integer> provinceMap = getDistrictMap();
        //将map存入redis
        try {
            String result = RedisUtil.set(redis, DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
            System.out.println("RedisUtil.set 结果: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setTest(BaseRedisDao baseRedisDao) {
        Map<String, Integer> provinceMap = getDistrictMap();
        //将对象存入redis
        baseRedisDao.set(DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
    }

    private static void setTest(RedisTemplate redisTemplate) {
        Map<String, Integer> provinceMap = getDistrictMap();
        //将对象存入redis
        //redisTemplate.boundValueOps(DISTRICT_PROVINCE_NAME_ID_MAP_KEY).set(provinceMap);
        redisTemplate.opsForValue().set(DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
    }

    private static void getTest(BaseRedisDao baseRedisDao) {
        //将对象存入redis
        Map<String, Integer> provinceMap = (Map<String, Integer>) baseRedisDao.get(DISTRICT_PROVINCE_NAME_ID_MAP_KEY);
        System.out.println(provinceMap);
    }

    private static Map<String, Integer> getDistrictMap() {
        RedisTest redisTest = new RedisTest();
        List<District> resultList = redisTest.getDatum();
        resultList.forEach(result -> System.out.println(result.getId()));
        //得到map
        Map<String, Integer> provinceMap = new HashMap<>();
        for (District district : resultList) {
            provinceMap.put(district.getName() + district.getSuffix(), district.getId());
        }
        return provinceMap;
    }

    public List getDatum() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DataSource dataSource = getDataSource();
        jdbcTemplate.setDataSource(dataSource);
        String sql = "select * from district where parent_id = 0";
        RowMapper rowMapper = new BeanPropertyRowMapper<>(District.class);
        List<District> resultList = jdbcTemplate.query(sql, null, rowMapper);
        return resultList;
    }

    public static DataSource getDataSource() {
        String name = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://139.196.165.243:3306/gateon_dmp_common";
        String user = "root";
        String password = "success";
        DataSource dataSource = new CustomDataSource(name, url, user, password);
        return dataSource;
    }

}
