package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.schedule.MyTaskTimer;

import java.util.Date;
import java.util.Timer;

public class Main {
    public static Class<?> clazz = ClassesConfig.getCurrentClass(Constant.CLASS_NAME, true, Constant.PATH_FILE);

    public static void main(String[] args) {
        MyTaskTimer schedule = new MyTaskTimer();
        // creating timer task, timer
        Timer timer = new Timer();
        timer.schedule(schedule, new Date(), 10_000);
//
//        // using with WatchEvent API
//        JedisPool jedisPool = JedisPoolFactory.getInstance();
//        AppUtil.watchEvent(jedisPool);

    }

    public static void changeValueClass(Class<?> clazzNew) {
        clazz = clazzNew;
    }
}
