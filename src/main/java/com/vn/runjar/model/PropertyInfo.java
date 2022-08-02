package com.vn.runjar.model;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.services.impl.AppServiceImpl;
import com.vn.runjar.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertyInfo {
    private static PropertyInfo _instance = null;
    public static String path = null;
    public static String period = null;

    public static void initialProperty(String key) {
        try {
            log.info("PropertyInfo constructor() START");
            String pathParent = Objects.requireNonNull(AppUtil.class.getResource("/")).getPath();
            log.info("PropertyInfo constructor() PATH : {}", pathParent);
            Path pathStr = Paths.get(pathParent).getParent().getParent().getParent().getParent();
            String url;
            String urlSub;
            if (Constant.MAIN_STRING.equals(key)) {
                urlSub = pathStr.getParent().getParent().getParent().toString();
            } else {
                urlSub = pathStr.toString();
                log.info("PropertyInfo constructor() URL SUB with main param: {}", urlSub);
            }
//            url = urlSub.substring(urlSub.indexOf("/")) + Constant.CONFIG_URL;
            url = "/home/tstran95/Public/WS/runtime-jar/runJARFileWithJersey/" + Constant.CONFIG_URL;
            log.info("PropertyInfo constructor() URL SUB with main param : {} , {}", url, key);

            InputStream is = Files.newInputStream(Paths.get(url));
            Properties props = new Properties();
            props.load(is);
            path = props.getProperty(Constant.PATH);
            period = props.getProperty(Constant.CONFIG_PERIOD);
            log.info("PropertyInfo constructor() END");
        } catch (Exception e) {
            throw new VNPAYException(Constant.PROPERTY_NOT_FOUND);
        }
    }

    public static PropertyInfo instance(String key) {
        if (_instance == null) {
            _instance = new PropertyInfo();
            initialProperty(key);
        }
        return _instance;
    }
}
