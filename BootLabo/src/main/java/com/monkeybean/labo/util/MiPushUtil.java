package com.monkeybean.labo.util;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Media;
import com.xiaomi.xmpush.server.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 小米消息推送
 * 接入文档：https://dev.mi.com/console/doc/detail?pId=1278
 * <p>
 * Created by MonkeyBean on 2019/2/21.
 */
public class MiPushUtil {
    private static Logger logger = LoggerFactory.getLogger(MiPushUtil.class);

    static {
        logger.info("MiPushUtil init");
        Constants.useSandbox();
    }

    public static String pushFile(File file, String appSecret, boolean isIcon) throws IOException {
        Media media = new Media(appSecret);
        Result result = media.upload(file, isIcon, true);
        logger.info("miPushFile, result data: {}", result.getData());
        String url;
        if (isIcon) {
            url = URLDecoder.decode(result.getData(Constants.JSON_MEDIA_ICON_URL), "UTF8");
        } else {
            url = URLDecoder.decode(result.getData(Constants.JSON_MEDIA_PICTURE_URL), "UTF8");
        }
        return url;
    }
}
