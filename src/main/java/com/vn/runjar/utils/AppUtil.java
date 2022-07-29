package com.vn.runjar.utils;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
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
            throw new VNPAYException(Constant.CHECK_SUM_ERROR);
        }
        log.info("AppUtil checkSum END with HEX STRING :  {}", result);
        return result.toString();
    }

    public static String getPath() {
//        return Paths.get(Objects.requireNonNull(AppUtil.class.getResource("/")).getPath())
//                    .getParent().getParent().getParent().getParent() + Constant.PATH;
        return Constant.PATH_FILE;
    }

    public static void watchEvent(JedisPool jedisPool) {
        log.info("AppUtil watchEvent START");
        try (Jedis jedis = jedisPool.getResource()) {
            WatchKey key;
            String path = AppUtil.getPath();

            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(path).getParent();

            //register a folder to WatchService to listen modify event in this folder
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                key = watcher.poll();

                // if key is null, don't have event in registered folder
                // else listen event, check modify event with jar file and load class in jar file again
                if (Objects.nonNull(key)) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        // Retrieve the type of event by using the kind() method.
                        WatchEvent.Kind<?> kind = event.kind();
                        //cast type of WatchEvent to Path
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path fileName = ev.context().getFileName();
                        // compare type of event with modify event
                        if ((kind == StandardWatchEventKinds.ENTRY_MODIFY)) {
                            // compare name of file was changed with name of file want check
                            // if same the name ->> load class again
                            if (Constant.JAR_FILE_NAME.equals(fileName.toString())) {
                                log.info("AppUtil watchEvent had MODIFIED the FILE NAME : {} ", fileName.getFileName());
                                System.err.printf("A file %s was modified.%n", fileName.getFileName());
                                jedis.hset(Constant.KEY_CHECK_CHANGE, Constant.STATUS_STR, Constant.STATUS_CHANGED);
                            }
                        }
                    }
                    // if reset() return false, key can't receive new event and break out of loop
                    if (!key.reset()) {
                        break;
                    }
                }
            }
            log.info("AppUtil watchEvent END");
        } catch (IOException e) {
            log.info("AppUtil watchEvent ERROR with exception " , e);
            e.printStackTrace();
        }
    }
}
