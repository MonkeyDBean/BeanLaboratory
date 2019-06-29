package com.monkeybean.labo.util;

import com.dd.plist.*;
import com.monkeybean.labo.component.reqres.res.IpaParseRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class IpaUtil {
    /**
     * 临时写入的字节长度
     */
    private static final int preSetByteSize = 1024;
    private static Logger logger = LoggerFactory.getLogger(IpaUtil.class);

    /**
     * 解析Plist文件
     *
     * @param in plist文件流
     * @return 失败返回null, 成功返回 k-v map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parsePListToMap(InputStream in) {
        if (in == null) {
            return null;
        }
        NSObject nsObject;
        try {
            nsObject = PropertyListParser.parse(in);
        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException e) {
            logger.error("parsePListToMap, Exception: {}", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("parsePListToMap, stream close IOException: {}", e);
            }
        }
        return (Map<String, Object>) nsObject.toJavaObject();
    }

    /**
     * 解析mobileprovision文件
     *
     * @param in 文件流
     * @return 失败返回null, 成功返回 k-v map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMobileProvisionToMap(InputStream in) {
        if (in == null) {
            return null;
        }
        StringBuilder originContentSBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String eachLine = br.readLine();
            while (eachLine != null) {
                originContentSBuilder.append(eachLine);
                eachLine = br.readLine();
            }
        } catch (IOException e) {
            logger.error("parseMobileProvisionToMap, IOException: {}", e);
        }

        //提取plist部分
        String startStr = "<plist";
        String endStr = "plist>";
        int i = originContentSBuilder.indexOf(startStr);
        int j = originContentSBuilder.indexOf(endStr);
        if (i == -1 || j == -1) {
            return null;
        }
        NSObject nsObject;
        try {
            nsObject = PropertyListParser.parse(originContentSBuilder.substring(i, j + endStr.length()).getBytes());
        } catch (IOException | PropertyListFormatException | ParseException | ParserConfigurationException | SAXException e) {
            logger.error("parseMobileProvisionToMap, Exception: {}", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("parseMobileProvisionToMap, stream close IOException: {}", e);
            }
        }
        return (Map<String, Object>) nsObject;
    }

    /**
     * 解密.mobileprovision文件并打印, linux系统下执行或Windows安装OpenSSL(安装参考:https://bbs.csdn.net/topics/392193545?page=1)
     * <p>
     * ios签名参考: https://blog.0x5e.cn/2016/09/25/ios-codesign-things/
     * 配置文件是.mobileprovision格式, 由苹果签名, 经过CMS(Cryptographic Message Syntax)加密的plist文件
     * 可以用OpenSSL或者macOS下的security工具进行解码
     * OpenSSL: openssl smime -inform der -verify -noverify -in example.mobileprovision
     * macOS: security cms -D -i example.mobileprovision
     *
     * @param fileName 全路径的文件名
     * @return 成功返回字符串格式的文件内容, 失败返回null
     */
    public static String getFileContentByExecuteShell(String fileName) {
        String cmd = "openssl smime -inform der -verify -noverify -in " + fileName;
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logger.error("getFileContentByExecuteShell, Runtime exec IOException: {}", e);
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
            logger.error("getFileContentByExecuteShell, StreamReader IOException: {}", e);
        }
        return originContentSBuilder.toString();
    }

    /**
     * 解析ipa文件
     * 参考: https://www.cnblogs.com/wjp1122/p/8984163.html
     *
     * @param file 需解析的文件
     * @return 成功返回所需信息, 失败返回null
     */
    public static IpaParseRes parseIpa(File file) {
        if (!file.exists()) {
            logger.warn("parseIpa, file not exist, param file path is: {}", file.getPath());
        } else {
            try (InputStream in = new FileInputStream(file); ZipInputStream zipIns = new ZipInputStream(in)) {
                IpaParseRes res = new IpaParseRes();
                ZipEntry zipEntry;
                while ((zipEntry = zipIns.getNextEntry()) != null) {
                    if (!zipEntry.isDirectory()) {
                        String entryName = zipEntry.getName();
                        if (entryName != null) {
                            String entryNameLower = entryName.toLowerCase();
                            if (entryNameLower.contains(".app/info.plist")) {

                                //当前条目的输入流转为字节输出流
                                int chunk;
                                byte[] tempStoreData = new byte[preSetByteSize];
                                ByteArrayOutputStream tempByteOuts = new ByteArrayOutputStream();
                                while ((chunk = zipIns.read(tempStoreData)) != -1) {
                                    tempByteOuts.write(tempStoreData, 0, chunk);
                                }

                                //解析为字典格式
                                NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(new ByteArrayInputStream(tempByteOuts.toByteArray()));

                                //打印词条
                                for (String key : rootDict.allKeys()) {
                                    logger.info(key + ":" + rootDict.get(key));
                                }

                                //设置所需信息
                                res.setBundleId(rootDict.get("CFBundleIdentifier").toString());
                                res.setBundleVersion(rootDict.get("CFBundleVersion").toString());
                                res.setDisplayName(rootDict.objectForKey("CFBundleDisplayName").toString());
                                res.setVersionNameOnAppStore(rootDict.objectForKey("CFBundleShortVersionString").toString());
                                res.setBundleName(rootDict.objectForKey("CFBundleName").toString());
                                res.setMinIOSVersion(rootDict.objectForKey("MinimumOSVersion").toString());

                                //获取图标名称
                                NSDictionary iconDict = (NSDictionary) rootDict.get("CFBundleIcons");
                                if (iconDict != null && iconDict.containsKey("CFBundlePrimaryIcon")) {
                                    NSDictionary CFBundlePrimaryIcon = (NSDictionary) iconDict.get("CFBundlePrimaryIcon");
                                    if (CFBundlePrimaryIcon.containsKey("CFBundleIconFiles")) {
                                        NSArray CFBundleIconFiles = (NSArray) CFBundlePrimaryIcon.get("CFBundleIconFiles");
                                        List<String> iconNameList = new ArrayList<>();
                                        for (NSObject icon : CFBundleIconFiles.getArray()) {
                                            String iconName = icon.toString();
                                            if (iconName.contains(".png")) {
                                                iconName = iconName.replace(".png", "");
                                            }
                                            iconNameList.add(iconName);
                                        }
                                        res.setIconNameList(iconNameList);
                                    }
                                }
                            } else if (entryNameLower.contains(".mobileprovision")) {
                                int chunkLength;
                                byte[] tempStoreData = new byte[preSetByteSize];
                                ByteArrayOutputStream tempByteOuts = new ByteArrayOutputStream();
                                while ((chunkLength = zipIns.read(tempStoreData)) != -1) {
                                    tempByteOuts.write(tempStoreData, 0, chunkLength);
                                }

                                //解析mobileprovision文件
                                Map<String, Object> map = parseMobileProvisionToMap(new ByteArrayInputStream(tempByteOuts.toByteArray()));

                                //打印词条
                                for (String key : map.keySet()) {
                                    logger.info(key + ":" + map.get(key));
                                }
                                res.setTeamName(map.get("TeamName").toString());
                            }
                        }
                    }
                }
                return res;
            } catch (Exception e) {
                logger.error("parseIpa, Exception: {}", e);
            }
        }
        return null;
    }

    /**
     * 根据图标名称下载图标文件到指定位置
     *
     * @param file     ipa文件
     * @param iconList 图标名称列表
     * @param path     图标存储路径
     */
    public static void parseAndStoreFile(File file, List<String> iconList, String path) {
        if (iconList == null || iconList.isEmpty()) {
            logger.warn("param iconList is null or empty");
            return;
        }
        try (ZipInputStream zipIns = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipIns.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    String entryName = zipEntry.getName();
                    for (String icon : iconList) {
                        if (entryName.contains(icon.trim())) {

                            //下载图片到指定路径
                            String[] entryNameSplit = entryName.split("/");
                            String imgPath = path + File.separator + entryNameSplit[entryNameSplit.length - 1];
                            try (FileOutputStream fos = new FileOutputStream(new File(imgPath))) {
                                int chunk;
                                byte[] data = new byte[preSetByteSize];
                                while ((chunk = zipIns.read(data)) != -1) {
                                    fos.write(data, 0, chunk);
                                }
                                logger.info("image download success, path: {}", imgPath);
                            } catch (IOException e) {
                                logger.error("parseAndStoreFile, FileOutputStream IOException: {}", e);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("parseAndStoreFile, InputStream IOException: {}", e);
        }
    }

}

