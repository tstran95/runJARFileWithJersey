package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.utils.AppUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        JedisPool jedisPool = JedisPoolFactory.generateJedisPoolFactory();
        Jedis jedis = jedisPool.getResource();

        String hex = AppUtil.checkSum(Constant.PATH);

        jedis.set(Constant.HEX_STRING , hex);
        jedis.close();
    }
}
