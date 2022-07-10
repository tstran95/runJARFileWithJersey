package com.vn.runjar.config;

import com.vn.runjar.constant.Constant;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassesConfig {
    /**
     * Find Class in JAR file by Path
     *
     * @return Class
     */
    public static Class<?> getCurrentClass(String className) {
        try {
            File fileName = new File(Constant.PATH);
            // get class loader parent
            ClassLoader parent = ClassesConfig.class.getClassLoader();
            // get url of file
            URL[] url = new URL[]{fileName.toURI().toURL()};
            //get URL Class loader child
            URLClassLoader child = new URLClassLoader(url , parent);

            return Class.forName(className, true, child);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
