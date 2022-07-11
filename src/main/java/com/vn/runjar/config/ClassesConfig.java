package com.vn.runjar.config;

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
    public static Class<?> getCurrentClass(String className , File file) {
        log.info("ClassesConfig getCurrentClass START with ClassName : {}" , className);
        try {
            // get class loader parent
            ClassLoader parent = ClassesConfig.class.getClassLoader();
            // get url of file
            URL[] url = new URL[]{file.toURI().toURL()};
            //get URL Class loader child
            URLClassLoader child = new URLClassLoader(url , parent);

            log.info("ClassesConfig getCurrentClass END with ClassName : {}" , className);
            return Class.forName(className, true, child);
        } catch (Exception e) {
            log.error("ClassesConfig getCurrentClass ERROR with e : " , e);
            return null;
        }
    }
}
