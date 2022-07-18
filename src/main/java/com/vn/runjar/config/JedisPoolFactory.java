package com.vn.runjar.config;


import com.vn.runjar.exception.VNPAYException;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolFactory {

    public static JedisPool generateJedisPoolFactory() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(1_00_000);
            poolConfig.setMaxIdle(1_00_000);
            poolConfig.setMinIdle(5);
            poolConfig.setMaxWaitMillis(100_000);
            // Whether to block when the connection is exhausted,
            // false will report an exception, true will block until the timeout
            poolConfig.setBlockWhenExhausted(Boolean.TRUE);
            return new JedisPool(poolConfig, "localhost", 6379, 100000);
        } catch (Exception e) {
            throw new VNPAYException("");
        }
    }
}
