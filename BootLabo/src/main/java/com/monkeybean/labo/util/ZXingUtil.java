package com.monkeybean.labo.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by MonkeyBean on 2018/7/12.
 * <p>
 * Google ZXing 二维码服务
 */
public class ZXingUtil {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static Logger logger = LoggerFactory.getLogger(ZXingUtil.class);

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static void writeToFile(BitMatrix matrix, String format, File file) {
        BufferedImage image = toBufferedImage(matrix);
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            logger.error("writeToFile, Could not write an image of format: {}, IOException: {}", format, e);
        }
    }

    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) {
        BufferedImage image = toBufferedImage(matrix);
        try {
            ImageIO.write(image, format, stream);
        } catch (IOException e) {
            logger.error("writeToStream, Could not write an image of format: {}, IOException: {}", format, e);
        }
    }

    private static BitMatrix generateBitMatrix(String content, Integer width, Integer height, boolean isFill) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        if (isFill) {
            hints.put(EncodeHintType.MARGIN, 0);
        }
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            logger.error("generateQRCode, WriterException: {}", e);
        }
        return bitMatrix;
    }

    /**
     * 生成二维码
     *
     * @param content 二维码内容，纯文本或链接地址
     * @param format  格式，如png, jpeg, jpg
     * @param width   图片宽度
     * @param height  图片高度
     * @param stream  输出流
     * @param isFill  二维码是否填充到整个图片
     */
    public static void generateQRCode(String content, String format, int width, int height, OutputStream stream, boolean isFill) {
        BitMatrix bitMatrix = generateBitMatrix(content, width, height, isFill);
        if (bitMatrix != null) {
            writeToStream(bitMatrix, format, stream);
        }
    }

    public static void generateQRCode(String content, String format, int width, int height, File file, boolean isFill) {
        BitMatrix bitMatrix = generateBitMatrix(content, width, height, isFill);
        if (bitMatrix != null) {
            writeToFile(bitMatrix, format, file);
        }
    }

    /**
     * 解析二维码图片
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String decodeQRCode(BufferedImage image) {
        MultiFormatReader formatReader = new MultiFormatReader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String codeContent = null;
        try {
            Result result = formatReader.decode(binaryBitmap, hints);
            codeContent = result.getText();
        } catch (NotFoundException e) {
            logger.error("NotFoundException: {}", e);
        }
        return codeContent;
    }

    /**
     * 添加logo，二维码有自我修复功能，可遮挡最多30%
     */
    public static BufferedImage addLogoToImage(BufferedImage image, String path) {
        Graphics2D g2 = image.createGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        try {
            BufferedImage logo = ImageIO.read(new File(path));

            //开始绘制，设置笔画对象
            g2.drawImage(logo, width / 5 * 2, height / 5 * 2, width / 5, height / 5, null);
            BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke);

            //指定弧度的圆角矩形，绘制
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(width / 5 * 2, height / 5 * 2, width / 5, height / 5, 20, 20);
            g2.setColor(Color.white);
            g2.draw(round);

            //设置灰色边框
            BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke2);
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(width / 5 * 2 + 2, height / 5 * 2 + 2, width / 5 - 4, height / 5 - 4, 20, 20);
            g2.setColor(new Color(128, 128, 128));
            g2.draw(round2);
            g2.dispose();
            image.flush();
        } catch (IOException e) {
            logger.error("IOException: {}", e);
        }
        return image;
    }
}
