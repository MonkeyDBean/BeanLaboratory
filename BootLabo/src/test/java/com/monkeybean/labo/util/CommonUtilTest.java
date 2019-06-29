package com.monkeybean.labo.util;

import org.junit.Test;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class CommonUtilTest {

    @Test
    public void resizeImage() {
        String srcPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\old\\AppIcon57x57.png";
        String destPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\new\\1_AppIcon512x512.png";
        CommonUtil.resizeImage(srcPath, destPath, 512, 512, true);
    }

    @Test
    public void changeSize() {
        String srcPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\old\\AppIcon57x57.png";
        String destPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\new\\2_AppIcon512x512.png";
        CommonUtil.changeSize(srcPath, destPath, 512, 512);
    }

}