package com.monkeybean.flux.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Created by MonkeyBean on 2018/11/21.
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 读取配置
     *
     * @param dirPath 文件夹路径
     */
    public static Properties loadPropertiesByNio(String dirPath) {
        Properties p = new Properties();
        if (dirPath == null) {
            logger.warn("loadPropertiesByNio, dirPath is null");
        } else {
            try {
                if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                    dirPath = dirPath.substring(1);
                }
                try (Stream<Path> path = Files.walk(Paths.get(dirPath))) {
                    path.forEach(eachFile -> {
                        try {
                            if (!eachFile.toFile().isDirectory()) {
                                p.load(Files.newBufferedReader(eachFile, StandardCharsets.UTF_8));
                            }
                        } catch (IOException e) {
                            logger.error("load properties, Files.newBufferedReader, IOException, {}", e);
                        }
                    });
                }
            } catch (IOException e) {
                logger.error("load properties, Files.walk, IOException, {}", e);
            }
        }
        return p;
    }

    /**
     * 读取配置
     *
     * @param dirPath 文件夹路径
     */
    public static Properties loadPropertiesByStream(String dirPath) {
        Properties p = new Properties();
        if (dirPath == null) {
            logger.warn("loadPropertiesByStream, dirPath is null");
        } else {
            File secretFileDir = new File(dirPath);
            File[] files = secretFileDir.listFiles();
            if (files == null) {
                logger.warn("loadPropertiesByStream, files is null");
            } else {
                try {
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            InputStream in = new BufferedInputStream(new FileInputStream(file));
                            p.load(in);
                            in.close();
                        }
                    }
                } catch (IOException e) {
                    logger.error("load properties, FileInputStream, IOException, {}", e);
                }
            }
        }
        return p;
    }
}
