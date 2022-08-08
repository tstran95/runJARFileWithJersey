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
        PropertyInfo.instance(Constant.MAIN_STRING , Constant.EMPTY, Constant.EMPTY);
        initClass(Constant.MAIN_STRING, Constant.EMPTY, Constant.EMPTY);
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

    public static Class<?> initClass(String key ,String libName , String className) {
        log.info("MAIN initClass() START");
        log.info("MAIN initClass() with Property CLASS NAME : {}" , PropertyInfo.clazzName);
        if (clazz == null) {
            PropertyInfo.initialProperty(key , libName , className);
            log.info("MAIN initClass() with Property CLASS NAME : {}" , PropertyInfo.clazzName);
            clazz = ClassesConfig.getCurrentClass(PropertyInfo.clazzName,
                    true,
                    PropertyInfo.path);
        }
        log.info("MAIN initClass() END");
        return clazz;
    }
}
