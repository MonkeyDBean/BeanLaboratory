package com.monkeybean.labo.util;

import com.dd.plist.*;
import com.monkeybean.labo.component.reqres.res.IpaParseRes;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
            logger.error("parsePListToMap, Exception: [{}]", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("parsePListToMap, stream close IOException: [{}]", e);
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
            logger.error("parseMobileProvisionToMap, IOException: [{}]", e);
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
            logger.error("parseMobileProvisionToMap, Exception: [{}]", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("parseMobileProvisionToMap, stream close IOException: [{}]", e);
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
            logger.error("getFileContentByExecuteShell, Runtime exec IOException: [{}]", e);
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
            logger.error("getFileContentByExecuteShell, StreamReader IOException: [{}]", e);
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
            logger.warn("parseIpa, file not exist, param file path is: [{}]", file.getPath());
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
                                    logger.debug(key + ":" + rootDict.get(key));
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
                                    logger.debug(key + ":" + map.get(key));
                                }
                                res.setTeamName(map.get("TeamName").toString());
                            }
                        }
                    }
                }
                return res;
            } catch (Exception e) {
                logger.error("parseIpa, Exception: [{}]", e);
            }
        }
        return null;
    }

    /**
     * 根据图标名称下载图标文件到指定位置
     *
     * @param file     ipa文件
     * @param iconList 图标名称列表
     * @param path     图标存储父路径
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
                                logger.debug("image download success, path: [{}]", imgPath);
                            } catch (IOException e) {
                                logger.error("parseAndStoreFile, FileOutputStream IOException: [{}]", e);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("parseAndStoreFile, InputStream IOException: [{}]", e);
        }
    }

    /**
     * 解析并更改plist文件内容
     *
     * @param originFile            原始plist文件
     * @param aimFile               处理后的文件
     * @param softwarePackageUrl    包下载路径
     * @param displayImageUrl       缩略图路径
     * @param fullSizeImageUrl      大图路径
     * @param bundleIdentifierValue 应用包名
     * @param bundleVersionValue    文件版本号
     * @param titleValue            应用展示名
     * @return 成功返回true, 失败返回false
     */
    public static synchronized boolean changePlistContent(File originFile, File aimFile, String softwarePackageUrl, String displayImageUrl, String fullSizeImageUrl,
                                                          String bundleIdentifierValue, String bundleVersionValue, String titleValue) {
        if (!originFile.exists()) {
            logger.warn("changePlistContent file not exist");
            return false;
        }
        try (InputStream in = new FileInputStream(originFile)) {
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(in);
            NSArray itemsArray = ((NSArray) rootDict.get("items"));
            NSDictionary itemsDic = (NSDictionary) (itemsArray.getArray()[0]);
            NSArray assetsArray = (NSArray) itemsDic.get("assets");
            for (NSObject eachItem : assetsArray.getArray()) {
                NSDictionary eachDic = (NSDictionary) eachItem;
                String kindValue = eachDic.get("kind").toString();
                if (kindValue.equals("software-package")) {
                    eachDic.put("url", softwarePackageUrl);
                } else if (kindValue.equals("display-image")) {
                    eachDic.put("url", displayImageUrl);
                } else if (kindValue.equals("full-size-image")) {
                    eachDic.put("url", fullSizeImageUrl);
                }
            }
            NSDictionary metaDataDic = (NSDictionary) itemsDic.get("metadata");
            metaDataDic.put("bundle-identifier", bundleIdentifierValue);
            metaDataDic.put("bundle-version", bundleVersionValue);
            metaDataDic.put("title", titleValue);

            //存储处理后的文件
            PropertyListParser.saveAsXML(rootDict, aimFile);
        } catch (Exception e) {
            logger.error("changePlistContent, Exception: [{}]", e);
            return false;
        }
        return true;
    }

    /**
     * 根据ipa文件生成目标文件夹(包含原始.ipa、manifest.plist以及及两个icon)
     *
     * @param ipaOriginFile     原始ipa文件
     * @param ipaOriginFileName 原始ipa文件名称, 格式如: ReD08C10X3_gymj_old_20190321_120900_107912_ios_dis.ipa; 不可从ipaOriginFile.getName()获取, ipaOriginFile可能为类型转换的临时文件(非原始文件)
     * @param domain            域名前缀, 格式如: https://home02.nm.erduosmj.com/package_ios
     * @param name              包名(标识符), 格式如ReDx001
     * @return 失败返回null; 成功返回map, 包含: 目标文件夹路径(desPath), 压缩文件的名称(compressName), 证书颁发商(teamName)
     */
    public static Map<String, Object> generateDirByIpa(File ipaOriginFile, String ipaOriginFileName, String domain, String name) {
        String[] originNameArray = ipaOriginFileName.split("_");
        if (originNameArray.length < 3) {
            logger.error("generateDirByIpa, MultipartFile name illegal");
            return null;
        }
        IpaParseRes res = IpaUtil.parseIpa(ipaOriginFile);
        if (res == null) {
            logger.error("parseIpa failed, IpaParseRes is null");
            return null;
        }
        final String imagePattern = ".png";
        String versionName = originNameArray[originNameArray.length - 3];
        String nameAndVersion = name + versionName;
        String urlPrefix = domain + "/" + nameAndVersion + "/";
        String softwarePackageUrl = urlPrefix + ipaOriginFileName;
        String displayImageName = "Icon-57" + imagePattern;
        String displayImageUrl = urlPrefix + displayImageName;
        String fullSizeImageName = "Icon-512" + imagePattern;
        String fullSizeImageUrl = urlPrefix + fullSizeImageName;
        URL manifestFileUrl = IpaUtil.class.getClassLoader().getResource("back_test/manifest.plist");
        if (manifestFileUrl == null) {
            logger.error("generateDirByIpa, manifest.plist is not exist");
            return null;
        }

        //plist原始文件
        File manifestOriginFile = new File(manifestFileUrl.getPath());

        //生成文件父路径
        //String newFileParentPath = ipaOriginFile.getParent() + File.separator + nameAndVersion;
        String newFileParentPath = manifestOriginFile.getParent() + File.separator + nameAndVersion;
        File parentDirFile = new File(newFileParentPath);

        //创建根目录, 若目录已存在, 则删除原有目录下的所有文件
        //本地应用运行目录(C:\Users\Administrator\AppData\Local\Temp)磁盘较满, 更改写入到target目录, linux下运行时, 不使用createTempFile, 预先配置临时源文件路径及目标文件路径
        if (parentDirFile.exists()) {
            File[] existFiles = parentDirFile.listFiles();
            if (existFiles != null) {
                for (File file : existFiles) {
                    boolean deleteChildFile = file.delete();
                    if (!deleteChildFile) {
                        logger.error("delete child file failed, parentDir: [{}], childDir: [{}]", newFileParentPath, file.getPath());
                        return null;
                    }
                }
            }
        } else {
            boolean createDir = parentDirFile.mkdir();
            if (!createDir) {
                logger.error("parentDirFile create failed: [{}]", newFileParentPath);
                return null;
            }
        }

        //生成manifest.plist文件
        File aimFile = new File(newFileParentPath + File.separator + manifestOriginFile.getName());
        IpaUtil.changePlistContent(manifestOriginFile, aimFile, softwarePackageUrl, displayImageUrl, fullSizeImageUrl, res.getBundleId(), res.getBundleVersion(), res.getDisplayName());

        //复制ipa文件到目标路径
        String ipaDesPathStr = newFileParentPath + File.separator + ipaOriginFileName;
        File ipaDesFile = new File(ipaDesPathStr);
//        if(ipaDesFile.exists()){
//            logger.warn("ipaDesFile is exist: [{}]", ipaDesPathStr);
//            boolean deleteIpaDesFile = ipaDesFile.delete();
//            if(!deleteIpaDesFile){
//                logger.error("ipaDesFile delete failed: [{}]", ipaDesPathStr);
//                return;
//            }
//        }
//        Files.copy(ipaOriginFile.toPath(), ipaDesFile.toPath());
        try {
            FileUtils.copyFile(ipaOriginFile, ipaDesFile);
        } catch (IOException e) {
            logger.error("generateDirByIpa copyFile IOException: [{}]", e);
            return null;
        }

        //复制所需图片
        List<String> originIconList = new ArrayList<>();
        String icon57OriginName = "AppIcon57x57" + imagePattern;
        String iconFullOriginName = "AppIcon83.5x83.5@2x~ipad" + imagePattern;
        originIconList.add(icon57OriginName);
        originIconList.add(iconFullOriginName);
        IpaUtil.parseAndStoreFile(ipaOriginFile, originIconList, newFileParentPath);

        //图片重命名
        String icon57OriginFilePath = newFileParentPath + File.separator + icon57OriginName;
        String displayImageFilePath = newFileParentPath + File.separator + displayImageName;
        File icon57OriginFile = new File(icon57OriginFilePath);
        File displayImageFile = new File(displayImageFilePath);
        String iconFullOriginFilePath = newFileParentPath + File.separator + iconFullOriginName;
        String fullSizeImageFilePath = newFileParentPath + File.separator + fullSizeImageName;
        File iconFullOriginFile = new File(iconFullOriginFilePath);
        File fullSizeImageFile = new File(fullSizeImageFilePath);

        //重命名文件不存在
        if (!icon57OriginFile.exists() || !iconFullOriginFile.exists()) {
            logger.error("icon57OriginFile or iconFullOriginFile not exist, icon57OriginFilePath: [{}], iconFullOriginFilePath: [{}]", icon57OriginFilePath, iconFullOriginFilePath);
            return null;
        }
        boolean icon57Rename = icon57OriginFile.renameTo(displayImageFile);
        boolean iconFullRename = iconFullOriginFile.renameTo(fullSizeImageFile);
        if (!icon57Rename || !iconFullRename) {
            logger.error("icon57OriginFile or iconFullOriginFile renameTo failed, icon57Rename: [{}], iconFullRename: [{}]", icon57Rename, iconFullRename);
            return null;
        }

        //更改图片宽高
        FileCommonUtil.resizeImage(fullSizeImageFilePath, fullSizeImageFilePath, 512, 512, true);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("desPath", newFileParentPath);
        dataMap.put("teamName", res.getTeamName());
        String compressName = originNameArray[0] + "-ios-ipa-" + versionName + ".tar.gz";
        dataMap.put("compressName", compressName);
        return dataMap;
    }

    /**
     * 复制inputStream流(InputStream不可重复读取), 方案如下
     * 1.InputStream转化为ByteArrayOutputStream
     * 2.需要使用InputStream对象时, 再从ByteArrayOutputStream转化: new ByteArrayInputStream(os.toByteArray())
     * 3.或生成临时文件, 每次从文件读取
     *
     * @param inputStream 输入流
     * @return 成功返回ByteArrayOutputStream, 失败返回null
     */
    public static ByteArrayOutputStream cloneInputStream(InputStream inputStream) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[preSetByteSize];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return os;
        } catch (IOException e) {
            logger.error("cloneInputStream IOException: [{}]", e);
        }
        return null;
    }

}

