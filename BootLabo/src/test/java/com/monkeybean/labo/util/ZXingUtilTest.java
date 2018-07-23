package com.monkeybean.labo.util;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

/**
 * Created by MonkeyBean on 2018/7/12.
 */
public class ZXingUtilTest {

    @Test
    public void decodeQRCode() throws Exception {
        String content = "http://monkeybean.cn/";
        String format = "png";
        String filePath = "C:/Users/Administrator/Desktop/test_img";
        File dirPath = new File(filePath);
        if (!dirPath.exists()) {
            System.out.println("create filePath " + filePath + "," + dirPath.mkdirs());
        }
        String imagePath = filePath + "/" + UUID.randomUUID().toString().substring(0, 8) + "." + format;
        File imageFile = new File(imagePath);

        //生成二维码写入文件
        ZXingUtil.generateQRCode(content, format, 200, 200, imageFile, true);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        //测试解码
        String codeContent = ZXingUtil.decodeQRCode(bufferedImage);
        System.out.println("decodeQRCode result : " + codeContent);

        //测试添加logo
        String logoName = "test_logo.png";
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL url = classLoader.getResource(logoName);
//        String logoPath = url.getFile();
        String logoPath = filePath + "/" + logoName;
        String addLogoPath = filePath + "/" + UUID.randomUUID().toString().substring(0, 8) + "." + format;
        File addLogoImage = new File(addLogoPath);
        ImageIO.write(ZXingUtil.addLogoToImage(bufferedImage, logoPath), format, addLogoImage);
    }

}