package com.vn.runjar.schedule;

import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.config.JedisPoolFactory;
import com.vn.runjar.utils.AppUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.TimerTask;

public class MyTaskTimer extends TimerTask {
    @Override
    public void run() {
        JedisPool jedisPool = JedisPoolFactory.getInstance();
        String path = AppUtil.getPath();

        // creat hex string of file
        String hexStr = AppUtil.checkSum(path);
        try (Jedis jedis = jedisPool.getResource();) {
            String hexSaved = jedis.hget("CHECK_CHANGE" , "checkSum");
            if (!hexStr.equals(hexSaved)) {
                jedis.hset("CHECK_CHANGE" , "checkSum" , hexStr);
                jedis.hset("CHECK_CHANGE" , "status" , "1");
            }
        }
    }
}
