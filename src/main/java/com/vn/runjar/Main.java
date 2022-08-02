package com.vn.runjar;

import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.model.PropertyInfo;
import com.vn.runjar.schedule.MyTaskTimer;
import com.vn.runjar.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Timer;
@Slf4j
public class Main {

    private static Class<?> clazz;


    public static void main(String[] args) {
        PropertyInfo.instance(Constant.MAIN_STRING);
        initClass();
        String time = PropertyInfo.period;
        MyTaskTimer schedule = new MyTaskTimer();
        // creating timer task, timer
        Timer timer = new Timer();
        timer.schedule(schedule, new Date(), AppUtil.parseLong(time));

//        // using with WatchEvent API
//        JedisPool jedisPool = JedisPoolFactory.getInstance();
//        AppUtil.watchEvent(jedisPool);
    }

    public static void changeValueClass(Class<?> clazzNew) {
        clazz = clazzNew;
    }

    public static Class<?> initClass() {
        log.info("MAIN initClass() START");
        if (clazz == null) {
            PropertyInfo.instance(Constant.MAIN_STRING);
            log.info("MAIN initClass() with Property PATH : {}" , PropertyInfo.path);
            clazz = ClassesConfig.getCurrentClass(Constant.CLASS_NAME,
                    true,
                    PropertyInfo.path);
        }
        log.info("MAIN initClass() END");
        return clazz;
    }
}
