package com.vn.runjar.services.impl;

import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.model.ClassInfo;
import com.vn.runjar.services.AppService;
import com.vn.runjar.utils.AppUtil;
import com.vn.runjar.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
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
    public void fly(ClassInfo classInfo) throws IOException {
        log.info("AppServiceImpl fly START with request {}", classInfo);
        try {
            Validator.checkInput(classInfo);
            int count = 0;
            String path = AppUtil.getPath();
            File fileName = new File(path);
            log.info("AppServiceImpl fly PATH {}", Paths.get(path));
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
        }
        catch (Exception e) {
            log.error("AppServiceImpl fly ERROR with ", e);
            throw e;
        }
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
            String path = AppUtil.getPath();

            // creat hex string of file
            String hexStr = AppUtil.checkSum(path);
            log.info("AppServiceImpl fly HEX {}", hexStr);

            // get current class in jar file
            Class<?> classLoader = ClassesConfig.getCurrentClass(className);

            while (true) {
                //get current hex string of this file
                String currentHex = AppUtil.checkSum(path);
                log.info("AppServiceImpl fly HEX {}", currentHex);

                // compare 2 string together
                // if 2 hex diff -> file replaced and get class in current JAR file again
                if (!currentHex.equals(hexStr)) {
                    log.info("CHANGE THE FILE");
                    hexStr = currentHex;
                    classLoader = ClassesConfig.getCurrentClass(className);
                }
                // run the method with input name in class has been found
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
     * Check the file has been replaced yet by checkSum
     * run method into jar file
     */
    public void flying(ClassInfo classInfo) throws IOException {
        log.info("AppServiceImpl flying START with request {}", classInfo);
        try {
            // validate input, check input not null
            Validator.checkInput(classInfo);
            int count = 0;
            WatchKey key;
            String className = classInfo.getClassName();
            String path = AppUtil.getPath();

            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(path).getParent();

            //register a folder to WatchService to listen modify event in this folder
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            // get current class in jar file
            Class<?> classLoader = ClassesConfig.getCurrentClass(className);

            while (true) {
                //retrieve and delete the next key or return null if any key does not exist
                key = watcher.poll();
                log.info("A key is : {}", key);

                // if key is null, don't have event in registered folder
                // else listen event, check modify event with file jar and load this class again
                if (Objects.nonNull(key)) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        // Retrieve the type of event by using the kind() method.
                        WatchEvent.Kind<?> kind = event.kind();
                        //cast type of WatchEvent to Path
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context();
                        // compare type of event with modify event
                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            // compare name of file was changed with name of file want check
                            // if same the name ->> load class again
                            if (Constant.JAR_FILE_NAME.equals(fileName.getFileName().toString())) {
                                log.info("A file {} was modified.", fileName.getFileName());
                                classLoader = ClassesConfig.getCurrentClass(className);
                            }
                        }
                    }
                    // if reset() return false, key can't receive new event and out the loop
                    if (!key.reset()) {
                        log.info("The Key not cant receive new event!!!!");
                        break;
                    }
                }
                // run the method with input name in class has been found
                this.fly(classLoader, classInfo.getMethodName());
                count++;
                log.info("-------------- " + count + " ----------------");
                log.info("AppServiceImpl flying END");
            }
        } catch (Exception e) {
            log.error("AppServiceImpl flying ERROR with ", e);
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