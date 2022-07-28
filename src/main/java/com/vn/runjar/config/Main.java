package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.schedule.MyTaskTimer;

import java.util.Date;
import java.util.Timer;

public class Main {
    static String path = Constant.PATH_FILE;
    public static Class<?> clazz = ClassesConfig.getCurrentClass(Constant.CLASS_NAME , true, path);
    public static void main(String[] args) {
        MyTaskTimer schedule = new MyTaskTimer();
        // creating timer task, timer
        Timer timer = new Timer();
        timer.schedule(schedule, new Date(), 10000);
    }
}
