package com.vn.runjar.services.impl;

import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.model.ClassInfo;
import com.vn.runjar.services.AppService;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

@Service
@Slf4j
public class AppServiceImpl implements AppService {
    private static AppServiceImpl instance;

    public static AppServiceImpl getInstance() {
        log.info("AppServiceImpl getInstance START");
        if (instance == null) {
            log.info("AppServiceImpl getInstance INSTANCE NULL");
            instance = new AppServiceImpl();
        }

        log.info("AppServiceImpl getInstance END");
        return instance;
    }

    @Override
    public void fly(ClassInfo classInfo) {
        log.info("AppServiceImpl fly START");
        try {
            int count = 0;
            File fileName = new File(Constant.PATH);
            log.info("AppServiceImpl fly PATH {}", Constant.PATH);
            String className = classInfo.getClassName();

            // get time modified file
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(fileName.toURI()), BasicFileAttributes.class);
            FileTime fileTime = attributes.lastAccessTime();
            log.info("AppServiceImpl fly FileTime {}", fileTime);
            // get current class
            Class<?> classLoader = ClassesConfig.getCurrentClass(className);

            while (true) {
                log.info("AppServiceImpl fly FileNAME {}", fileName);
                FileTime currentAccessFileTime = Files.readAttributes(Paths.get(fileName.toURI()), BasicFileAttributes.class)
                        .lastAccessTime();
                log.info("AppServiceImpl fly FileTime {}", currentAccessFileTime);
                // check access time of this JAR file
                // if 2 time diff -> file replaced and get class in current JAR file again
                if (!currentAccessFileTime.equals(fileTime)) {
                    log.info("CHANGE THE FILE");
                    log.info("AppServiceImpl fly FileTime {}", fileTime);
                    log.info("AppServiceImpl fly FileTime {}", currentAccessFileTime);
                    classLoader = ClassesConfig.getCurrentClass(className);
                }
                this.fly(classLoader, classInfo.getMethodName());
                count++;
                log.info("-------------- " + count + " ----------------");
                log.info("AppServiceImpl fly END");
            }
        } catch (Exception e) {
            log.error("AppServiceImpl fly ERROR with ", e);
        }
    }

    /**
     * Run method in JAR file
     */
    private void fly(Class<?> classLoaded, String classMethod) {
        log.info("AppServiceImpl method private of fly() START");
        try {
            // get Method in class by name
            Method method = classLoaded.getDeclaredMethod(classMethod);
            // create instance of class
            Object instance = classLoaded.getDeclaredConstructor().newInstance();
            method.invoke(instance);
            Thread.sleep(1000);
            log.info("AppServiceImpl method private of fly() END");
        } catch (Exception e) {
            log.error("AppServiceImpl method private of fly() ERROR With MESSAGE ", e);
        }
    }
}
