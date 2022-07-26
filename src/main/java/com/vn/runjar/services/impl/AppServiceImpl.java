package com.vn.runjar.services.impl;

import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.config.JedisPoolFactory;
import com.vn.runjar.config.Main;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.model.ClassInfo;
import com.vn.runjar.services.AppService;
import com.vn.runjar.utils.AppUtil;
import com.vn.runjar.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

@Service
@Slf4j
public class AppServiceImpl implements AppService {
    private static AppServiceImpl instance;

    /**
     * create new instance of AppServiceImpl
     */
    public static AppServiceImpl getInstance() {
        log.info("AppServiceImpl getInstance START");
        if (instance == null) {
            log.info("AppServiceImpl getInstance INSTANCE NULL");
            instance = new AppServiceImpl();
        }

        log.info("AppServiceImpl getInstance END");
        return instance;
    }

    /**
     * run method into jar file
     * Check the file has been replaced yet by time access of file
     */
    @Deprecated
    @Override
    public void fly(ClassInfo classInfo) {
        log.info("AppServiceImpl fly START with request {}", classInfo);
        try {
            int count = 0;
            File fileName = new File(AppUtil.getPath());
            log.info("AppServiceImpl fly PATH {}", Paths.get(AppUtil.getPath()));
            String className = classInfo.getClassName();

            // get time modified file
            BasicFileAttributes attributes = Files.readAttributes(Paths.get(fileName.toURI()), BasicFileAttributes.class);
            FileTime fileTime = attributes.lastAccessTime();
            log.info("AppServiceImpl fly FileTime {}", fileTime);
            // get current class
            Class<?> classLoader = ClassesConfig.getCurrentClass(className);

            while (true) {
                log.info("AppServiceImpl fly FileNAME {}", fileName);
                FileTime currentAccessFileTime = Files.readAttributes(Paths.get(fileName.toURI()),
                                BasicFileAttributes.class)
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
        log.info("AppServiceImpl fly END with request {}", classInfo);
    }

    /**
     * Check the file has been replaced yet by checkSum
     * run method into jar file
     */
    public void flyAgain(ClassInfo classInfo) {
        log.info("AppServiceImpl fly START with request {}", classInfo);
        try {
            Validator.checkInput(classInfo);
            int count = 0;
            String className = classInfo.getClassName();

            // creat hex string of file
            String hexStr = AppUtil.checkSum(AppUtil.getPath());
            log.info("AppServiceImpl fly HEX {}", hexStr);

            // get current class in jar file
            Class<?> classLoader = ClassesConfig.getCurrentClass(className);

            while (true) {
                //get current hex string of this file
                String currentHex = AppUtil.checkSum(AppUtil.getPath());
                log.info("AppServiceImpl fly HEX {}", currentHex);

                // compare 2 string together
                // if 2 hex diff -> file replaced and get class in current JAR file again
                if (!currentHex.equals(hexStr)) {
                    log.info("CHANGE THE FILE");
                    hexStr = currentHex;
                    classLoader = ClassesConfig.getCurrentClass(className);
                }
                // run the method with input name in class has been founded
                this.fly(classLoader, classInfo.getMethodName());
                count++;
                log.info("-------------- " + count + " ----------------");
                log.info("AppServiceImpl fly END");
            }
        } catch (Exception e) {
            log.error("AppServiceImpl flyAgain ERROR with ", e);
            throw e;
        }
    }

    /**
     * Run method in JAR file
     */
    public void fly(Class<?> classLoaded, String classMethod) {
        log.info("AppServiceImpl method private of fly() START");
        try {
            // get Method in class by name
            Method method = classLoaded.getDeclaredMethod(classMethod);
            // create instance of class
            Object instance = classLoaded.getDeclaredConstructor().newInstance();
            // and run method in this class
            method.invoke(instance);
            Thread.sleep(2000);
            log.info("AppServiceImpl method private of fly() END");
        } catch (Exception e) {
            log.error("AppServiceImpl method private of fly() ERROR With MESSAGE ", e);
            throw new VNPAYException(Constant.INVOKE_FALSE);
        }
    }
}
