package com.vick.test.util.redis;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RedisUtil {
    public static void main(String[] args) {
        RedisConfig redisConfig;
        /*RedisConfig gateonDmpDev = new RedisConfig("192.168.16.239", 6390, "GateonDmpDev");
        redisConfig = gateonDmpDev;*/
        RedisConfig vick = new RedisConfig("139.196.165.243", 6379, "success");
        redisConfig = vick;
        Jedis redis = connect(redisConfig);
        selectDb(redis, 2);
        //optString(redis);
        optMap(redis);
    }

    public static BaseRedisDao getBaseRedisDao(String ip, int port, String password, int targetDb) {
        RedisTemplate redisTemplate = RedisUtil.getRedisTemplate(ip, port, password, targetDb);
        return new RedisHandle(redisTemplate);
    }

    public static RedisTemplate getRedisTemplate(String ip, int port, String password, int targetDb) {
        JedisConnectionFactory jedisConnectionFactory = RedisUtil.jedisConnectionFactory(ip, port, password, targetDb);
        RedisConfiguration redisConfiguration = new RedisConfiguration();
        RedisSerializer redisSerializer = redisConfiguration.fastJson2JsonRedisSerializer();
        RedisTemplate<String, String> redisTemplate = redisConfiguration.redisTemplate(jedisConnectionFactory, redisSerializer);
        //RedisTemplate redisTemplate = new RedisTemplate();
        //redisTemplate.setDefaultSerializer(new Jackson2JsonRedisSerializer(Object.class));
        //redisTemplate.setConnectionFactory(jedisConnectionFactory);
        /**必须执行这个函数,初始化RedisTemplate*/
        //redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public static RedisStandaloneConfiguration redisStandaloneConfiguration(String ip, int port, String password, int targetDb) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(ip);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(targetDb);
        redisStandaloneConfiguration.setPassword(password);
        return redisStandaloneConfiguration;
    }

    public static JedisConnectionFactory jedisConnectionFactory(String ip, int port, String password, int targetDb) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = redisStandaloneConfiguration(ip, port, password, targetDb);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return jedisConnectionFactory;
    }

    @Deprecated
    public static Jedis getConnect(String ip, int port, String password, int targetDb) {
        RedisConfig redisConfig;
        RedisConfig vick = new RedisConfig(ip, port, password);
        redisConfig = vick;
        Jedis redis = connect(redisConfig);
        selectDb(redis, targetDb);
        return redis;
    }

    @Deprecated
    public static Jedis connect(RedisConfig redisConfig) {
        String ip = redisConfig.getIp();
        int port = redisConfig.getPort();
        String password = redisConfig.getPassword();

        //连接本地的 RedisTemplate 服务
        Jedis jedis = new Jedis(ip, port);
        // 如果 RedisTemplate 服务设置来密码，需要下面这行，没有就不需要
        if (null != password && password.length() >= 0) {
            jedis.auth(password);
        }
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

    @Deprecated
    public static void selectDb(Jedis jedis, int db) {
        jedis.select(db);
        System.out.println("redis 已切换到db: " + db);
    }

    public static void optMap(Jedis jedis) {
        //System.out.println("redis 存储的字符串为: " + jedis.hgetAll("district:provinceid:city:name:id"));
        System.out.println("redis 存储的字符串为: " + jedis.hgetAll("district:cityid:district:name:id"));
    }

    public static void optString(Jedis jedis) {
        //设置 redis 字符串数据
        //jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: " + jedis.get("district:province:name:id"));
    }

    public static String hmset(Jedis redis, String key, Map<String, ? extends Object> map) throws IOException {
        if (map == null) {
            redis.hmset(key, null);
            return null;
        }
        Map<String, Object> valuesMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            valuesMap.put(entry.getKey(), entry.getValue());
        }
        String serialize = SerializeUtil.serialize(valuesMap);
        String result = redis.set(key, serialize);
        return result;
    }

    public static Object hmget(Jedis redis, String key) throws ClassNotFoundException, IOException {
        Map<String, String> map = redis.hgetAll(key);
        Map<String, Object> valuesMap = new HashMap<>();
        int stringFlag = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (stringFlag == 0) {
                if (entry.getValue() instanceof String) {
                    stringFlag = 1;
                } else {
                    stringFlag = -1;
                }
            }
            if (stringFlag == 1) {
                valuesMap.put(entry.getKey(), entry.getValue());
            } else {
                Integer integer = (Integer) SerializeUtil.serializeToObject(entry.getValue());
                valuesMap.put(entry.getKey(), integer);
            }
            Integer integer = (Integer) SerializeUtil.serializeToObject(entry.getValue());
            valuesMap.put(entry.getKey(), integer);
        }
        return valuesMap;
    }

    public static String set(Jedis redis, String key, Object obj) throws IOException {
        if (obj == null) {
            redis.set(key, null);
            return null;
        }
        String serializedObj = SerializeUtil.serialize(obj);

        String result = redis.set(key, serializedObj);
        return result;
    }

    @Deprecated
    private static class RedisConfig {
        private String ip;
        private int port;
        private String password;

        public RedisConfig(String ip, int port, String password) {
            this.ip = ip;
            this.port = port;
            this.password = password;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public String getPassword() {
            return password;
        }
    }


}
