package com.monkeybean.labo.util;

import org.junit.Test;

import java.io.File;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class FileCommonUtilTest {

    @Test
    public void resizeImage() {
        String srcPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\old\\AppIcon57x57.png";
        String destPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\new\\1_AppIcon512x512.png";
        FileCommonUtil.resizeImage(srcPath, destPath, 512, 512, true);
    }

    @Test
    public void changeSize() {
        String srcPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\old\\AppIcon57x57.png";
        String destPath = "C:\\Users\\Administrator\\Desktop\\test_img_store\\new\\2_AppIcon512x512.png";
        FileCommonUtil.changeSize(srcPath, destPath, 512, 512);
    }

    @Test
    public void compressToTar() {
        String srcPath = "E:\\my_document\\BeanLaboratory\\BootLabo\\target\\classes\\back_test\\ReDx001107912";
        String desPath = "E:\\my_document\\BeanLaboratory\\BootLabo\\target\\classes\\back_test\\ReDx001107912\\test.tar.gz";
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            FileCommonUtil.compressToTar(srcFile, desPath);
        }
    }

}