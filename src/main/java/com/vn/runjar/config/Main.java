package com.vn.runjar.config;

import java.util.Date;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        MyTimerTask schedule = new MyTimerTask();
        // creating timer task, timer
        Timer timer = new Timer();
        timer.schedule(schedule, new Date(), 10000);
    }
}
