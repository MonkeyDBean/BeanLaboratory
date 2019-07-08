package com.monkeybean.labo.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件相关操作
 * <p>
 * Created by MonkeyBean on 2019/3/20.
 */
public class FileCommonUtil {
    private static Logger logger = LoggerFactory.getLogger(FileCommonUtil.class);

    /**
     * 文件大小合法性判断
     *
     * @param length  文件字节长度
     * @param maxSize 文件最大字节长度限制，若为1以下则不限制大小
     * @return true为合法
     */
    public static boolean isFileSizeLegal(int length, Double maxSize) {
        Double size = length / 1024.;
        return maxSize <= 1 || maxSize - size > 0.001;
    }

    /**
     * 生成文件存储路径
     *
     * @param rootPath   根路径
     * @param hash       文件hash
     * @param nameSuffix 文件名后缀
     */
    public static String generateMappingPath(String rootPath, String hash, String nameSuffix) {
        if (StringUtils.isEmpty(hash) || hash.length() < 32) {
            return "";
        }
        String filePath = rootPath + File.separator
                + hash.substring(0, 3) + File.separator
                + hash.substring(3, 6) + File.separator
                + hash.substring(6, 9) + File.separator;

        //目录不存在则创建
        File tempDir = new File(filePath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return filePath + hash.substring(9) + "." + nameSuffix;
    }

    /**
     * 文件加密，仅简单异或
     *
     * @param fileBytes 文件字节数组
     * @param xor       异或数字
     */
    public static byte[] encryptImgXor(byte[] fileBytes, int xor) {
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] = (byte) (fileBytes[i] ^ xor);
        }
        return fileBytes;
    }

    /**
     * 读取文件内容
     * jdk1.7之后, 优雅关闭外部资源的编写方式(实际为语法糖, 编译后依然是之前的finally写法): try-with-resource
     * 在之前的编程, 如果打开外部资源, 使用完毕后必须手动关闭, 因为外部资源不由JVM管理
     * 参考: https://www.cnblogs.com/itZhy/p/7636615.html
     *
     * @param file 文件对象
     * @return 成功返回字符串, 失败返回null
     */
    public static String getContent(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line = "";
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        } catch (IOException e) {
            logger.error("getReader IOException： {}", e);
            return null;
        }
    }

    /**
     * 调整图片宽高, 重新生成图片
     *
     * @param srcPath   原图片路径
     * @param destPath  新生成的图片路径
     * @param newWidth  图片新宽度
     * @param newHeight 图片新高度
     * @param forceSize 是否强制使用指定宽、高: false为保持原图片宽高比例约束
     * @return 成功返回true
     */
    public static boolean resizeImage(String srcPath, String destPath, int newWidth, int newHeight, boolean forceSize) {
        try {
            if (forceSize) {
                Thumbnails.of(srcPath).forceSize(newWidth, newHeight).toFile(destPath);
            } else {
                Thumbnails.of(srcPath).width(newWidth).height(newHeight).toFile(destPath);
            }
        } catch (IOException e) {
            logger.error("resizeImage, IOException: [{}]", e);
            return false;
        }
        return true;
    }

    /**
     * 更改图片宽高, 不推荐使用
     * 作用同resizeImage方法, 但生成图片的质量远不如resizeImage方法(Thumbnails依赖), 且有时出现生成图片全黑的情况
     *
     * @param srcPath   原图片路径
     * @param destPath  新生成的图片路径
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 成功返回true
     */
    public static boolean changeSize(String srcPath, String destPath, int newWidth, int newHeight) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcPath)); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destPath))) {

            //字节流转图片对象
            Image image = ImageIO.read(in);

            //构建图片流
            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

            //绘制改变尺寸后的图
            bufferedImage.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);
            ImageIO.write(bufferedImage, "PNG", out);
            return true;
        } catch (IOException e) {
            logger.error("changeSize, IOException: [{}]", e);
            return false;
        }
    }

    /**
     * 将给定目录压缩为tar.gz
     *
     * @param inputFile   待压缩文件
     * @param outFilePath 压缩后的文件路径
     * @return 成功返回true, 失败返回false
     */
    public static boolean compressToTar(File inputFile, String outFilePath) {
        if (inputFile == null || !inputFile.isDirectory()) {
            logger.error("inputFile is null or not directory");
            return false;
        }
        File outputFile = new File(outFilePath);
        if (outputFile.exists()) {
            boolean deleteExist = outputFile.delete();
            if (!deleteExist) {
                logger.error("deleteExist failed, outFilePath: [{}]", outFilePath);
            }
        }
        File outputFileParent = new File(outputFile.getParent());
        if (!outputFileParent.exists()) {
            logger.error("outFilePath parent dir not exist");
            return false;
        }
        File[] sourceFiles = inputFile.listFiles();
        if (sourceFiles == null || sourceFiles.length == 0) {
            logger.error("sourceFiles is null or empty");
            return false;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFilePath);
             CompressorOutputStream gzippedOut = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, fileOutputStream);
             TarArchiveOutputStream os = new TarArchiveOutputStream(gzippedOut)) {
            os.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File file : sourceFiles) {

                //设置添加到tar包的文件目录为相对路径
                String p = file.getParentFile().getParent();
                String pn = file.getAbsolutePath().substring(p.length() + 1);
                ArchiveEntry archiveEntry = new TarArchiveEntry(file, pn);
                os.putArchiveEntry(archiveEntry);
                IOUtils.copy(new FileInputStream(file), os);
                os.closeArchiveEntry();
            }
        } catch (Exception e) {
            logger.error("compressToTar Exception: [{}]", e);
            return false;
        }
        return true;
    }
}
