package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;

import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
//        MyTimerTask schedule = new MyTimerTask();
//        // creating timer task, timer
//        Timer timer = new Timer();
//        timer.schedule(schedule, new Date(), 10000);
        System.out.println(Paths.get(Objects.requireNonNull(Main.class.getResource("/")).getPath()).getParent().getParent() + Constant.PATH);
    }
}
