package com.vn.runjar.services.impl;

import com.vn.runjar.Main;
import com.vn.runjar.config.ClassesConfig;
import com.vn.runjar.config.JedisPoolFactory;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.model.ClassInfo;
import com.vn.runjar.model.PropertyInfo;
import com.vn.runjar.response.Response;
import com.vn.runjar.services.AppService;
import com.vn.runjar.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;

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
     * Check the file has been replaced yet by checkSum
     * run method into jar file
     */
    public Response run(ClassInfo classInfo) {
        log.info("AppServiceImpl method run() START with request {}", classInfo);
        PropertyInfo.instance(Constant.APP_STRING);
        String path = PropertyInfo.path;
        JedisPool jedisPool = JedisPoolFactory.getInstance();
        //the next time, check data in redis : if file jar was modified, load class again
        try (Jedis jedis = jedisPool.getResource()) {
            // validate input
            Validator.checkInput(classInfo);
            // load Class from Main
            Class<?> classLoaded = Main.initClass();
            log.info("AppServiceImpl method run() RUNNING with Class {}", classLoaded);

            String status = jedis.hget(Constant.KEY_CHECK_CHANGE, Constant.STATUS_STR);
            log.info("STATUS IN REDIS : {}", status);
            if (Constant.STATUS_CHANGED.equals(status)) {
                // load class again
                classLoaded = ClassesConfig.getCurrentClass(classInfo.getClassName(), false, path);
                // and set status in redis is 'not change: 0'
                // set value for class in Main
                jedis.hset(Constant.KEY_CHECK_CHANGE, Constant.STATUS_STR, Constant.STATUS_DEFAULT);
                Main.changeValueClass(classLoaded);
                log.info("THE FILE HAD BEEN CHANGED");
            }
            //invoke method into jar file
            String message = this.invokeMethod(classLoaded, classInfo.getMethodName());
            log.info("AppServiceImpl method run() END with request {}", classInfo);
            return Response.getResponse(Constant.OK, Constant.SUCCESS , message , classInfo.getTokenID());
        } catch (Exception e) {
            log.info("AppServiceImpl method run() ERROR with error ", e);
            return Response.getResponse(Constant.ERROR, e.getMessage() , Constant.FAIL , classInfo.getTokenID());
        }
    }


    /**
     * Run method in JAR file
     */
    private String invokeMethod(Class<?> classLoaded, String classMethod) {
        log.info("AppServiceImpl method private of fly() START");
        try {
            String result;
            // get Method in class by name
            Method method = classLoaded.getDeclaredMethod(classMethod);
            // create instance of class
            Object instance = classLoaded.getDeclaredConstructor().newInstance();
            // and run method in this class
            result = (String) method.invoke(instance);
            log.info("AppServiceImpl method private of fly() END with Object : {}", result);
            return result;
        } catch (Exception e) {
            log.error("AppServiceImpl method private of fly() ERROR With MESSAGE ", e);
            throw new VNPAYException(Constant.INVOKE_FALSE);
        }
    }
}
