package com.vick.test.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        RedisConfig redisConfig;
        RedisConfig gateonDmpDev = new RedisConfig("192.168.16.239", 6390, "GateonDmpDev");
        redisConfig = gateonDmpDev;
        /*RedisConfig vick = new RedisConfig("139.196.165.243", 6379, "success");
        redisConfig = vick;*/
        Jedis redis = connect(redisConfig);
        selectDb(redis, 2);
        //optString(redis);
        optMap(redis);
    }


    public static Jedis connect(RedisConfig redisConfig) {
        String ip = redisConfig.getIp();
        int port = redisConfig.getPort();
        String password = redisConfig.getPassword();

        //连接本地的 Redis 服务
        Jedis jedis = new Jedis(ip, port);
        // 如果 Redis 服务设置来密码，需要下面这行，没有就不需要
        if (null != password && password.length() >= 0) {
            jedis.auth(password);
        }
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

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
