package com.vick.test.util.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisConfiguration {

    public RedisSerializer fastJson2JsonRedisSerializer() {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory, RedisSerializer fastJson2JsonRedisSerializer) {
        //StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        //redis   开启事务
//        redisTemplate.setEnableTransactionSupport(true);
        //StringRedisSerializer  key  序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash  使用jdk  的序列化
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
        //keySerializer  对key的默认序列化器。默认值是StringSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //  valueSerializer
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
