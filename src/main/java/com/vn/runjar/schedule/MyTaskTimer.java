package com.vn.runjar.schedule;

import com.vn.runjar.config.JedisPoolFactory;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.utils.AppUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.TimerTask;

public class MyTaskTimer extends TimerTask {
    @Override
    public void run() {
        JedisPool jedisPool = JedisPoolFactory.getInstance();
        String path = AppUtil.getPath();
        // check By Sum
        checkBySum(jedisPool , path);
    }

    private void checkBySum(JedisPool jedisPool , String path) {
        // creat hex string of file
        String hexStr = AppUtil.checkSum(path);
        try (Jedis jedis = jedisPool.getResource();) {
            String hexSaved = jedis.hget(Constant.KEY_CHECK_CHANGE, Constant.CHECK_SUM_STR);
            if (!hexStr.equals(hexSaved)) {
                jedis.hset(Constant.KEY_CHECK_CHANGE, Constant.CHECK_SUM_STR , hexStr);
                jedis.hset(Constant.KEY_CHECK_CHANGE, Constant.STATUS_STR , Constant.STATUS_CHANGED);
            }
        }
    }

    private void checkByWatchEvent() {

    }
}
