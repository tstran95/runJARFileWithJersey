package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.schedule.MyTaskTimer;
import com.vn.runjar.utils.AppUtil;

import java.util.Date;
import java.util.Objects;
import java.util.Timer;

public class Main {
    public static Class<?> clazz = ClassesConfig.getCurrentClass(Constant.CLASS_NAME, true, AppUtil.getPropertiesValue(Constant.PATH));

    public static void main(String[] args) {
        String time = AppUtil.getPropertiesValue(Constant.CONFIG_PERIOD);
        MyTaskTimer schedule = new MyTaskTimer();
        // creating timer task, timer
        Timer timer = new Timer();
        timer.schedule(schedule, new Date(), AppUtil.parseLong(time));
//
//        // using with WatchEvent API
//        JedisPool jedisPool = JedisPoolFactory.getInstance();
//        AppUtil.watchEvent(jedisPool);
    }

    public static void changeValueClass(Class<?> clazzNew) {
        clazz = clazzNew;
    }
}
