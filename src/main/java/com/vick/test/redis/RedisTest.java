package com.vick.test.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        Jedis redis = connect();
        selectDb(redis, 5);
        optString(redis);
    }

    public static Jedis connect() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("192.168.16.239", 6390);
        // 如果 Redis 服务设置来密码，需要下面这行，没有就不需要
        jedis.auth("GateonDmpDev");
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: " + jedis.ping());
        return jedis;
    }

    public static void selectDb(Jedis jedis, int db) {
        jedis.select(db);
        System.out.println("redis 已切换到db: " + db);
    }

    public static void optString(Jedis jedis) {
        //设置 redis 字符串数据
        //jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: " + jedis.get("HB"));
    }


}
