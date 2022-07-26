package com.vn.runjar.config;

import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
//        MyTimerTask schedule = new MyTimerTask();
//        // creating timer task, timer
//        Timer timer = new Timer();
//        timer.schedule(schedule, new Date(), 10000);
        String path = Objects.requireNonNull(Main.class.getResource("/")).getPath();
        System.out.println(path.substring(0 , path.lastIndexOf("/target")));
        String newPath = path.substring(0 , path.lastIndexOf("/target")) + "/lib/flyWithMe.jar";
        System.out.println(new File(newPath));
    }
}
