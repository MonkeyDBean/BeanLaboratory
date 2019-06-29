package com.monkeybean.labo.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;

/**
 * 其他通用工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

    /**
     * 获取数字某位的值
     *
     * @param num   整数数字
     * @param index 非负数, 0:个位, 1:十位, 2:百位...
     * @return 返回数字某位，参数不合法则返回0
     */
    public static int getNum(int num, int index) {
        return (num / (int) Math.pow(10, index)) % 10;
    }

    /**
     * 格式化为小数点两位
     */
    public static String getString2(String str) {
        Double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String getString2(int value) {
        return getString2(String.valueOf(value));
    }

    public static String getString2(double value) {
        return getString2(String.valueOf(value));
    }

    /**
     * 检查密码是否过于简单, 6-20位，包含字母和数字，该方法可用正则替换
     */
    public static boolean checkPassword(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        int length = str.length();
        if (length < 6 || length > 20)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            } else {
                return false;
            }
        }
        return isDigit && isLetter;
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
     * 生成cron表达式字符串
     *
     * @param hour   时
     * @param minute 分
     * @param week   每周几
     * @param day    每月几号
     * @param type   类型, 1为每天定点执行, 2为每周定点执行, 3为每月定点执行
     * @return cron表达式
     */
    public static String generateCronStr(int hour, int minute, int week, int day, int type) {
        String cronStr;
        switch (type) {
            case 1:
                cronStr = String.format("0 %s %s * * ?", minute, hour);
                break;
            case 2:
                cronStr = String.format("0 %s %s ? * %s", minute, hour, week);
                break;
            case 3:
                cronStr = String.format("0 %s %s %s * ?", minute, hour, day);
                break;
            default:
                cronStr = "0 0 0 * * ?";
        }
        return cronStr;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param url      主机名
     * @param username 账号
     * @param password 密码
     * @param path     保存目录
     * @param filename 文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, String username, String password, String path, String filename, InputStream input) {
        FTPClient ftp = new FTPClient();
        try {

            //连接FTP服务器
            ftp.connect(url);

            //登录
            ftp.login(username, password);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }
            if (!ftp.changeWorkingDirectory(path)) {

                //路径不存在则创建
                if (!ftp.makeDirectory(path)) {
                    logger.error("FTP uploadFile, make dir error");
                    return false;
                }
                ftp.changeWorkingDirectory(path);
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.storeFile(filename, input);
            input.close();
            ftp.logout();
        } catch (IOException e) {
            logger.error("FTP uploadFile, IOException: {}", e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    logger.error("FTP uploadFile, close IOException: {}", e);
                }
            }
        }
        return true;
    }

    /**
     * 调用脚本, 读取执行结果
     *
     * @param filePath 脚本路径, 脚本文件格式可以为bat, shell, python
     * @return 失败返回null
     */
    public static String callScript(String filePath) {

        //若为python脚本, 加命令前缀
        //解析python文件，也可考虑引入jython依赖(https://mvnrepository.com/artifact/org.python/jython-standalone), 调用PythonInterpreter
        String format = filePath.substring(filePath.length() - 2, filePath.length());
        if ("py".equalsIgnoreCase(format)) {
            filePath = "python " + filePath;
        }
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(filePath);
        } catch (IOException e) {
            logger.error("callScript Runtime exec IOException: {}", e);
            return null;
        }
        StringBuilder originContentSBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String eachLine = br.readLine();
            while (eachLine != null) {
                originContentSBuilder.append(eachLine);
                eachLine = br.readLine();
            }
        } catch (IOException e) {
            logger.error("callScript StreamReader IOException: {}", e);
        }
        return originContentSBuilder.toString();
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
            logger.error("resizeImage, IOException: {}", e);
            return false;
        }
        return true;
    }

    /**
     * 更改图片宽高
     * 作用同resizeImage方法, 但生成图片的质量远不如resizeImage方法(Thumbnails依赖)
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
            logger.error("changeSize, IOException: {}", e);
            return false;
        }
    }

}
