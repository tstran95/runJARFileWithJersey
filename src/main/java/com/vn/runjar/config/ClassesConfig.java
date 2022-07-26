package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

@Slf4j
public class ClassesConfig {
    /**
     * Find Class in JAR file by Path
     *
     * @return Class
     */
    public static Class<?> getCurrentClass(String className) {
        log.info("ClassesConfig getCurrentClass START with ClassName : {}" , className);
        try {
            File fileName = new File(AppUtil.getPath());
            // get class loader parent
            ClassLoader parent = ClassesConfig.class.getClassLoader();
            // get url of file
            URL[] url = new URL[]{fileName.toURI().toURL()};
            //get URL Class loader child
            URLClassLoader child = new URLClassLoader(url , parent);

            log.info("ClassesConfig getCurrentClass END with ClassName : {}" , className);
            return Class.forName(className, true, child);
        } catch (Exception e) {
            log.error("ClassesConfig getCurrentClass ERROR with e : " , e);
            throw new VNPAYException(Constant.CLASS_NOT_FOUND);
        }
    }
}
