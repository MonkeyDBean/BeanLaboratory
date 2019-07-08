package com.monkeybean.labo.util;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    /**
     * ftpClient连接池初始化标志
     */
    private static volatile boolean hasInit = false;

    /**
     * ftpClient连接池
     */
    private static ObjectPool<FTPClient> ftpClientPool;

    /**
     * 文件存储的基础路径, 初始化为根路径
     */
    private static String basePath = "/";

    /**
     * 初始化ftpClientPool
     *
     * @param ftpClientPool ftp连接池
     * @param basePath      文件基础路径, 若为空, 表示根路径, 则不设值
     */
    public static void init(ObjectPool<FTPClient> ftpClientPool, String basePath) {
        if (!hasInit) {
            synchronized (FtpUtil.class) {
                if (!hasInit) {
                    FtpUtil.ftpClientPool = ftpClientPool;
                    hasInit = true;
                    if (basePath != null) {
                        FtpUtil.basePath = basePath;
                    }
                }
            }
        }
    }

    /**
     * 获取ftpClient
     */
    private static FTPClient getFtpClient() {
        checkFtpClientPoolAvailable();
        FTPClient ftpClient = null;
        Exception ex = null;

        // 获取连接, 最多尝试3次
        for (int i = 0; i < 3; i++) {
            try {
                ftpClient = ftpClientPool.borrowObject();
                if (!ftpClient.changeWorkingDirectory(basePath)) {
                    ftpClient.makeDirectory(basePath);
                }
                break;
            } catch (Exception e) {
                ex = e;
            }
        }
        if (ftpClient == null) {
            logger.error("Could not get a ftpClient from the pool: [{}]", ex);
        }
        return ftpClient;
    }

    /**
     * 释放ftpClient
     */
    private static void releaseFtpClient(FTPClient ftpClient) {
        if (ftpClient == null) {
            return;
        }
        try {
            ftpClientPool.returnObject(ftpClient);
        } catch (Exception e) {
            logger.error("ftpClientPool, returnObject Exception: [{}]", e);
        } finally {
            if (ftpClient.isAvailable()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    logger.error("ftpClient, disconnect IOException: [{}]", e);
                }
            }
        }
    }

    /**
     * 检查ftpClientPool是否可用
     */
    private static void checkFtpClientPoolAvailable() {
        Assert.state(hasInit, "FTP unable or connect error");
    }

    /**
     * 判断文件是否存在ftp目录上
     *
     * @param pathAndName 文件全路径
     * @return 存在返回true, 不存在或异常返回false
     */
    public static boolean existFile(String pathAndName) {
        String path = getFileParentPath(pathAndName);
        String fileName = pathAndName.substring(pathAndName.lastIndexOf("/") + 1);
        FTPClient ftpClient = getFtpClient();
        if (ftpClient == null) {
            return false;
        }
        try {
            List<String> files = retrieveFileNames(path);
            for (String file : files) {
                if (file.equals(fileName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("existFile, path: [{}], fileName: [{}], Exception: [{}]", path, fileName, e);
        } finally {
            releaseFtpClient(ftpClient);
        }
        return false;
    }

    /**
     * 上传文件到FTP, 连接池方式
     *
     * @param inputStream    文件输入流
     * @param remoteFilePath 包含文件名称的文件存储路径, 如a.txt, temp/a.txt
     * @param override       若文件已存在(根据同一目录下的文件名判断), 是否强制覆盖
     * @return 成功返回true, 失败或异常返回false
     */
    public static boolean uploadFileToFtp(InputStream inputStream, String remoteFilePath, boolean override) {
        Assert.notNull(inputStream, "inputStream cannot be null");
        Assert.hasText(remoteFilePath, "remoteFilePath cannot be null or blank");
        FTPClient ftpClient = getFtpClient();
        if (ftpClient == null) {
            return false;
        }
        String path = getFileParentPath(remoteFilePath);
        String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
        try {
            if (!StringUtils.isEmpty(path) && !StringUtils.isEmpty(fileName) && createDir(path)) {
                if (!override) {
                    List<String> files = retrieveFileNames(path);
                    if (!files.isEmpty()) {
                        for (String eachFile : files) {
                            if (eachFile.equals(fileName)) {
                                logger.info("file is exist, remoteFilePath: [{}]", remoteFilePath);
                                return true;
                            }
                        }
                    }
                }
                return ftpClient.storeFile(remoteFilePath, inputStream);
            }
        } catch (IOException e) {
            logger.error("file is exist, remoteFilePath: [{}]", remoteFilePath);
        } finally {
            releaseFtpClient(ftpClient);
        }
        return false;
    }

    /**
     * 从远程的一个目录往远程的另一个目录move文件
     *
     * @param sourceFilePath 原始文件
     * @param destFilePath   目标文件
     */
    public static boolean moveFileToDirectory(String sourceFilePath, String destFilePath) {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(destFilePath)) {
            logger.warn("moveFileToDirectory, param illegal, sourceFilePath: [{}], destFilePath: [{}]", sourceFilePath, destFilePath);
            return false;
        }
        if (sourceFilePath.equals(destFilePath)) {
            logger.info("sourceFilePath equals to destFilePath, no need to move");
            return true;
        }
        FTPClient ftpClient = getFtpClient();
        try {
            if (existFile(sourceFilePath)) {
                String path = getFileParentPath(destFilePath);
                if (createDir(path)) {
                    if (ftpClient.rename(sourceFilePath, destFilePath)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("move file error, Exception: [{}]", e);
        }
        return false;
    }

    /**
     * 删除远程ftp文件
     *
     * @return 成功返回true, 失败返回false
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath) || "/".equals(filePath)) {
            logger.warn("deleteFile, param illegal, filePath: [{}]", filePath);
            return false;
        }
        FTPClient ftpClient = getFtpClient();
        try {
            if (existFile(filePath)) {
                if (ftpClient.deleteFile(filePath)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("delete file error, filePath: [{}], e: [{}]", filePath, e);
        }
        return false;
    }

    /**
     * 创建目录(有则切换目录，没有则创建目录), 根目录则直接返回
     *
     * @param dir 目录名称
     * @return 成功返回true, 失败返回false
     */
    private static boolean createDir(String dir) {
        if (StringUtils.isEmpty(dir) || "/".equals(dir)) {
            return true;
        }
        FTPClient ftpClient = getFtpClient();
        if (ftpClient == null) {
            return false;
        }
        try {

            //尝试切入目录
            if (ftpClient.changeWorkingDirectory(dir)) {
                return true;
            }
            String[] dirArray = dir.split("/");
            StringBuilder currentDir = new StringBuilder();

            //循环生成子目录
            for (String eachDir : dirArray) {
                if (StringUtils.isEmpty(eachDir)) {
                    continue;
                }
                currentDir.append("/");
                currentDir.append(eachDir);

                //尝试切入目录
                if (ftpClient.changeWorkingDirectory(currentDir.toString()))
                    continue;
                if (!ftpClient.makeDirectory(currentDir.toString())) {
                    logger.error("create dir failed：[{}]", currentDir.toString());
                    return false;
                }
            }

            //将目录切换至指定路径
            return ftpClient.changeWorkingDirectory(dir);
        } catch (Exception e) {
            logger.error("ftp create dir Exception: [{}]", e);
            return false;
        } finally {
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 获取指定路径下FTP文件名称
     *
     * @param remotePath 目录路径
     * @return ftp文件名称列表
     */
    private static List<String> retrieveFileNames(String remotePath) {
        FTPFile[] ftpFiles = retrieveFTPFiles(remotePath);
        if (ftpFiles == null || ftpFiles.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(ftpFiles).filter(Objects::nonNull)
                .map(FTPFile::getName).collect(Collectors.toList());
    }

    /**
     * 获取指定路径下FTP文件
     *
     * @param remotePath 路径
     * @return 成功返回FTPFile数组, 失败返回null
     */
    private static FTPFile[] retrieveFTPFiles(String remotePath) {
        FTPClient ftpClient = getFtpClient();
        if (ftpClient != null) {
            try {
                return ftpClient.listFiles(remotePath + "/", file -> file != null && file.getSize() > 0);
            } catch (IOException e) {
                logger.error("retrieveFTPFiles, IOException: [{}]", e);
            } finally {
                releaseFtpClient(ftpClient);
            }
        }
        return null;
    }

    /**
     * 获取文件所在目录路径
     */
    private static String getFileParentPath(String filePath) {
        return filePath.lastIndexOf("/") == -1 ? "/" : filePath.substring(0, filePath.lastIndexOf("/"));
    }

    /**
     * 编码文件路径, 若包含中文路径, 需编码
     */
    private static String encodingPath(String path) throws UnsupportedEncodingException {

        // FTP协议规定文件名编码为iso-8859-1, 若路径中包含中文, 则目录名或文件名需要转码
        return new String(path.replaceAll("//", "/").getBytes("GBK"), CharEncoding.ISO_8859_1);
    }

    /**
     * 向FTP服务器上传文件, 非连接池方式
     *
     * @param url         主机名, 如: ftp-cdn.itops.monkeytest.com
     * @param username    账号
     * @param password    密码
     * @param path        保存目录, 若为根目录, 参数传 /
     * @param filename    文件名
     * @param inputStream 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url, String username, String password, String path, String filename, InputStream inputStream) {
        FTPClient ftp = new FTPClient();
        boolean executeResult = false;
        try {

            //连接FTP服务器
            ftp.connect(url);

            //登录
            ftp.login(username, password);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.error("uploadFile, ftp reply is negative: [{}]", reply);
                return executeResult;
            }
            if (!ftp.changeWorkingDirectory(path)) {

                //路径不存在则创建
                if (!ftp.makeDirectory(path)) {
                    logger.error("uploadFile, make dir error");
                    return executeResult;
                }
                ftp.changeWorkingDirectory(path);
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setControlEncoding(CharEncoding.UTF_8);
            ftp.enterLocalPassiveMode();
            ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            boolean storeResult = ftp.storeFile(filename, inputStream);
            if (storeResult) {
                executeResult = true;
            } else {
                logger.error("uploadFile, storeResult failed");
            }
            ftp.logout();
        } catch (IOException e) {
            logger.error("uploadFile, IOException: [{}]", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("uploadFile, inputStream close IOException: [{}]", e);
                }
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    logger.error("uploadFile, disconnect IOException: [{}]", e);
                }
            }
        }
        return executeResult;
    }

}
