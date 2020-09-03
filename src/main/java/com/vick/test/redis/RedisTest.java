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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vick Zhang
 * @since 2020/9/1
 */
public class RedisTest {

    /**
     * 地区服务省名和省ID Map key
     */
    private static final String DISTRICT_PROVINCE_NAME_ID_MAP_KEY = "district:province:name:id";
    /**
     * 地区服务市名和市ID Map key
     */
    private static final String DISTRICT_PROVINCEID_CITY_NAME_ID_MAP_KEY = "district:provinceid:city:name:id";
    /**
     * 地区服务区县名和区县ID Map key
     */
    private static final String DISTRICT_CITY_DISTRICT_NAME_ID_MAP_KEY = "district:cityid:district:name:id";

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
        } catch (ClassNotFoundException | IOException e) {
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
        Map<String, Integer> provinceMap = new HashMap<>();
        Map<String, Map<String, Integer>> provinceIdToCityMap = new HashMap<>();
        Map<String, Map<String, Integer>> cityIdToDistrictMap = new HashMap<>();
        getDistrictMap(provinceMap, provinceIdToCityMap, cityIdToDistrictMap);
        //将对象存入redis
        //baseRedisDao.set(DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
        baseRedisDao.addMap(DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
        baseRedisDao.addMap(DISTRICT_PROVINCEID_CITY_NAME_ID_MAP_KEY, provinceIdToCityMap);
        baseRedisDao.addMap(DISTRICT_CITY_DISTRICT_NAME_ID_MAP_KEY, cityIdToDistrictMap);
    }

    private static void setTest(RedisTemplate redisTemplate) {
        Map<String, Integer> provinceMap = getDistrictMap();
        //将对象存入redis
        redisTemplate.boundValueOps(DISTRICT_PROVINCE_NAME_ID_MAP_KEY).set(provinceMap);
        //redisTemplate.opsForValue().set(DISTRICT_PROVINCE_NAME_ID_MAP_KEY, provinceMap);
    }

    private static void getTest(BaseRedisDao baseRedisDao) {
        //将对象存入redis
        //Map<String, Integer> provinceMap = (Map<String, Integer>) baseRedisDao.get(DISTRICT_PROVINCE_NAME_ID_MAP_KEY);
        Map provinceMap = baseRedisDao.getMap(DISTRICT_PROVINCE_NAME_ID_MAP_KEY);
        System.out.println(provinceMap);
    }

    private static Map<String, Integer> getDistrictMap() {
        RedisTest redisTest = new RedisTest();
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        //省
        List<District> resultList = redisTest.getProvinceData(jdbcTemplate);
        //得到map
        Map<String, Integer> provinceMap = new HashMap<>();
        for (District district : resultList) {
            provinceMap.put(district.getName() + district.getSuffix(), district.getId());
        }
        return provinceMap;
    }

    private static void getDistrictMap(Map<String, Integer> provinceMap, Map<String, Map<String, Integer>> provinceIdToCityMap,
                                       Map<String, Map<String, Integer>> cityIdToDistrictMap) {
        RedisTest redisTest = new RedisTest();
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        //省
        List<District> resultList = redisTest.getProvinceData(jdbcTemplate);
        //resultList.forEach(result -> System.out.println(result.getId()));
        //得到map
        List<Integer> provinceIdList = new ArrayList<>();
        for (District district : resultList) {
            provinceMap.put(district.getName() + district.getSuffix(), district.getId());
            provinceIdList.add(district.getId());
        }
        //市
        RowMapper rowMapper = new BeanPropertyRowMapper<>(District.class);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < provinceIdList.size(); i++) {
            if (i < provinceIdList.size() - 1) {
                sb.append(provinceIdList.get(i)).append(",");
            } else {
                sb.append(provinceIdList.get(i));
            }
        }
        String sql = "select * from district where parent_id in (" + sb.toString() + ")";
        List<District> cityList = jdbcTemplate.query(sql, null, rowMapper);

        List<Integer> cityIdList = new ArrayList<>();
        for (District district : cityList) {
            assembleDistrictMap(provinceIdToCityMap, district);
            cityIdList.add(district.getId());
        }
        //县
        sb = new StringBuffer();
        for (int i = 0; i < cityIdList.size(); i++) {
            if (i < cityIdList.size() - 1) {
                sb.append(cityIdList.get(i)).append(",");
            } else {
                sb.append(cityIdList.get(i));
            }
        }
        sql = "select * from district where parent_id in (" + sb.toString() + ")";
        List<District> districtList = jdbcTemplate.query(sql, null, rowMapper);
        for (District district : districtList) {
            assembleDistrictMap(cityIdToDistrictMap, district);
        }
    }

    private static void assembleDistrictMap(Map<String, Map<String, Integer>> provinceIdToCityMap, District district) {
        String provinceId = String.valueOf(district.getParentId());
        Map<String, Integer> cityName_cityIdMap = provinceIdToCityMap.get(provinceId);
        if (cityName_cityIdMap == null) {
            cityName_cityIdMap = new HashMap<>();
            provinceIdToCityMap.put(provinceId, cityName_cityIdMap);
        }
        cityName_cityIdMap.put(district.getName() + district.getSuffix(), district.getId());
    }

    private List getProvinceData(JdbcTemplate jdbcTemplate) {
        String sql = "select * from district where parent_id = 0";
        RowMapper rowMapper = new BeanPropertyRowMapper<>(District.class);
        List<District> resultList = jdbcTemplate.query(sql, null, rowMapper);
        return resultList;
    }

    private static JdbcTemplate getJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        DataSource dataSource = getDataSource();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    private static DataSource getDataSource() {
        String name = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://139.196.165.243:3306/gateon_dmp_common";
        String user = "root";
        String password = "success";
        return new CustomDataSource(name, url, user, password);
    }

}
