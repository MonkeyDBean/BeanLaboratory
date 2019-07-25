package com.monkeybean.dynamicds.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 读取application配置
     */
    public static Properties getProperties() {
        Properties props = new Properties();
        try {
            final String fileSuffix = ".properties";
            String rootFileName = "application";
            File rootFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + rootFileName + fileSuffix);
            InputStream rootIn = new FileInputStream(rootFile);
            props.load(rootIn);
            rootIn.close();
            String activeFile = props.get("spring.profiles.active").toString();
            props.clear();
            File aimFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + rootFileName + "-" + activeFile + fileSuffix);
            InputStream aimIn = new FileInputStream(aimFile);
            props.load(aimIn);
            aimIn.close();
        } catch (Exception e) {
            logger.error("PropertiesUtil, getProperties error: [{}]", e);
        }
        return props;
    }

}
