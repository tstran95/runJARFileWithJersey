package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
public class ClassesConfig {

    /**
     * Find Class in JAR file by Path
     *
     * @return Class
     */
    public static Class<?> getCurrentClass(String className , boolean status , String path) {
        log.info("ClassesConfig getCurrentClass START with ClassName : {}" , className);
        try {
            File fileName = new File(path);
            System.out.println(fileName);
            // get class loader parent
            ClassLoader parent = ClassesConfig.class.getClassLoader();
            // get url of file
            URL[] url = new URL[]{fileName.toURI().toURL()};
            //get URL Class loader child
            URLClassLoader child = new URLClassLoader(url , parent);

            log.info("ClassesConfig getCurrentClass END with ClassName : {}" , className);
            return Class.forName(className, status, child);
        } catch (ClassNotFoundException e) {
            log.error("ClassesConfig getCurrentClass ERROR with e : " , e);
            throw new VNPAYException(Constant.CLASS_NOT_FOUND);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
