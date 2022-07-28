package com.vn.runjar.utils;

import com.vn.runjar.config.Main;
import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Objects;

@Slf4j
public class AppUtil {
    public static String checkSum(String path) {
        log.info("AppUtil checkSum START with PATH :  {}", path);
        StringBuilder result = new StringBuilder();
        try {
            // file hashing with DigestInputStream
            MessageDigest md = MessageDigest.getInstance(Constant.ALGORITHM); //SHA, MD2, MD5, SHA-256, SHA-384...
            try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(Paths.get(path)), md)) {
                while (dis.read() != -1) ; //empty loop to clear the data
                md = dis.getMessageDigest();
            }

            // bytes to hex
            for (byte b : md.digest()) {
                result.append(String.format(Constant.FORMAT, b));
            }
        } catch (Exception e) {
            log.info("AppUtil checkSum ERROR with error : ", e);
//            throw new VNPAYException(Constant.CHECK_SUM_ERROR);
            checkSum("/home/tstran95/Public/WS/runtime-jar/runJARFileWithJersey/lib/flyWithMe.jar");
        }
        log.info("AppUtil checkSum END with HEX STRING :  {}", result);
        return result.toString();
    }

    public static String getPath() {
//        return Paths.get(Objects.requireNonNull(AppUtil.class.getResource("/")).getPath())
//                    .getParent().getParent().getParent().getParent() + Constant.PATH;
        return "/home/tstran95/Public/WS/runtime-jar/runJARFileWithJersey/lib/flyWithMe.jar";
    }
}
