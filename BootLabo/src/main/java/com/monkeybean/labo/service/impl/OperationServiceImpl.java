package com.monkeybean.labo.service.impl;

import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.res.ImageInfoRes;
import com.monkeybean.labo.component.reqres.res.OtherProjectInfoRes;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.service.OperationService;
import com.monkeybean.labo.service.PublicService;
import com.monkeybean.labo.service.database.LaboDoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作相关的服务
 * <p>
 * Created by MonkeyBean on 2019/1/4.
 */
@Service
public class OperationServiceImpl implements OperationService {
    private static Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final LaboDoService laboDoService;

    private final OtherConfig otherConfig;

    private final PublicService publicService;

    @Autowired
    public OperationServiceImpl(LaboDoService laboDoService, OtherConfig otherConfig, PublicService publicService) {
        this.laboDoService = laboDoService;
        this.otherConfig = otherConfig;
        this.publicService = publicService;
    }

    /**
     * 上传图片
     *
     * @param accountId 账户Id
     * @param fileCode  base64编码的文件
     * @param fileName  文件名
     * @return 成功返回图片url
     */
    public Result<String> imageUpload(int accountId, final String fileCode, String fileName) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);

        //校验账户合法性
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("imageUpload, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //图片格式统一为小写
        String imageFileName = fileName.substring(0, fileName.lastIndexOf("."));
        String formatSuffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = imageFileName + formatSuffix.toLowerCase();

        //文件大小校验
        byte[] fileBytes = Base64.decodeBase64(fileCode.getBytes());
        String fileSizeKb = String.format("%.2f", fileBytes.length / 1024.) + " kb";
        logger.info("imageUpload, accountId: {}, fileName: {}, size: {}", accountId, fileName, fileSizeKb);
        if (!this.isFileSizeLegal(fileBytes.length)) {
            logger.warn("image size is too large: {}", fileSizeKb);
            return new Result<>(ReturnCode.FILE_TOO_LARGE);
        }
        String accessPath;
        String storePath;
        String fileMd5 = DigestUtils.md5Hex(fileCode);

        //限制：同一账户md5相同的图片不重复入库
        if (!laboDoService.queryImageListByHash(fileMd5, accountId).isEmpty()) {
            logger.info("imageUpload, file is exist, accountId: {}, fileMd5: {}", accountId, fileMd5);
            return new Result<>(ReturnCode.FILE_HAS_EXIST);
        }

        //md5相同的图片不重复存储
        List<Map<String, Object>> dbImageInfoList = laboDoService.queryImageListByHash(fileMd5, null);
        if (!dbImageInfoList.isEmpty()) {
            Map<String, Object> dbImageInfo = dbImageInfoList.get(0);
            accessPath = dbImageInfo.get("access_path").toString();
            storePath = dbImageInfo.get("store_path").toString();
        } else {

            //新建图片文件夹
            storePath = otherConfig.getBaseStorePath() + "/" + fileMd5;
            File rootPathDir = new File(storePath);
            if (!rootPathDir.exists()) {
                logger.info("rootPathDir is created: {}, status: {}", rootPathDir, rootPathDir.mkdirs());
            }
            String fileStorePath = rootPathDir.getPath() + "/" + fileName;
            File imageFile = new File(fileStorePath);
            try {
                try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                    outputStream.write(fileBytes);
                    outputStream.flush();
                }
                logger.info("file store success, accountId: {}, fileName: {}, fileCode: {}", accountId, fileName, fileCode);
            } catch (IOException e) {
                logger.error("file IOException: {}", e);
                return new Result<>(ReturnCode.SERVER_EXCEPTION);
            }
            accessPath = otherConfig.getBaseAccessPath() + "/" + fileMd5 + "/" + fileName;
        }

        //入库
        laboDoService.addImageInfo(accountId, imageFileName, fileMd5, storePath, accessPath);
        return new Result<>(ReturnCode.SUCCESS, accessPath);
    }

    /**
     * 判断文件大小是否合法
     *
     * @param length 文件大小，字节数组长度
     * @return 合法返回true
     */
    private boolean isFileSizeLegal(int length) {
        int maxSize = otherConfig.getImageMaxSize() * 1024 * 1024;
        return length < maxSize;
    }

    /**
     * 获取图片列表
     * 当前记录数未达请求页数，返回最后页数数据
     *
     * @param accountId   账户Id
     * @param shareType   图片类型，0为账户私有，1为公共，2为账户所有
     * @param currentPage 当前页，首页为1
     * @param pageSize    每页记录数量
     */
    public Result<Map<String, Object>> getImageList(int accountId, int shareType, int currentPage, int pageSize) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("getImageList, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        List<ImageInfoRes> returnList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        Integer queryAccountId = accountId;
        Integer queryShareType = shareType;
        if (shareType == ConstValue.IMAGE_ACCESS_SHARE) {
            queryAccountId = null;
        } else if (shareType == ConstValue.IMAGE_ACCESS_ALL) {
            queryShareType = null;
        }
        Integer totalCount = laboDoService.queryImageCountByShareType(queryShareType, queryAccountId);
        if (totalCount != 0) {
            int offset;
            if (totalCount <= (currentPage - 1) * pageSize) {
                offset = totalCount / pageSize * pageSize;
            } else {
                offset = pageSize * (currentPage - 1);
            }
            List<Map<String, Object>> imageList = laboDoService.queryImageListByShareType(queryShareType, queryAccountId, pageSize, offset);
            for (Map<String, Object> eachImage : imageList) {
                ImageInfoRes eachImageRes = new ImageInfoRes();
                eachImageRes.setId(Integer.parseInt(eachImage.get("id").toString()));
                eachImageRes.setFileName(eachImage.get("file_name").toString());
                eachImageRes.setFileDes((String) eachImage.get("file_des"));
                eachImageRes.setAccessUrl(eachImage.get("access_path").toString());
                eachImageRes.setShare(Boolean.parseBoolean(eachImage.get("is_share").toString()));
                eachImageRes.setUploadTime(eachImage.get("createTime_format").toString());
                returnList.add(eachImageRes);
            }
        }
        data.put("totalCount", totalCount);
        data.put("imageList", returnList);
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    /**
     * 更新图片名称及描述
     *
     * @param accountId 账户Id
     * @param imageId   图片Id
     * @param fileName  文件名
     * @param fileDes   文件描述
     */
    public Result<String> changeImageInfo(int accountId, int imageId, String fileName, String fileDes) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("changeImageInfo, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        List<Map<String, Object>> imageList = laboDoService.queryImageById(imageId, accountId);
        if (imageList.isEmpty()) {
            logger.warn("changeImageInfo, image: {} doesn't belong to accountId: {}", imageId, accountId);
            return new Result<>(ReturnCode.FILE_NOT_MINE);
        }
        laboDoService.updateImageInfo(imageId, fileName, fileDes);
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 更改图片状态
     *
     * @param accountId   账户Id
     * @param imageIds    图片Id列表
     * @param operateType 操作类型，1为共享状态更改，2为删除
     */
    public Result<String> changeImageStatus(int accountId, List<Integer> imageIds, int operateType) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("changeImageStatus, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        if (operateType == 2) {
            laboDoService.removeImages(imageIds, accountId);
        } else {
            List<Map<String, Object>> imageStatusList = laboDoService.queryImageShareStatusList(imageIds, accountId);
            List<Integer> shareImageIds = new ArrayList<>();
            List<Integer> unShareImageIds = new ArrayList<>();
            for (Map<String, Object> eachImage : imageStatusList) {
                Integer imageId = Integer.valueOf(eachImage.get("id").toString());
                if (Boolean.parseBoolean(eachImage.get("is_share").toString())) {
                    unShareImageIds.add(imageId);
                } else {
                    shareImageIds.add(imageId);
                }
            }
            if (!unShareImageIds.isEmpty()) {
                laboDoService.changeImageShareStatus(unShareImageIds, accountId, ConstValue.IMAGE_ACCESS_PRIVATE);
            }
            if (!shareImageIds.isEmpty()) {
                laboDoService.changeImageShareStatus(shareImageIds, accountId, ConstValue.IMAGE_ACCESS_SHARE);
            }
        }
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 多图存储
     *
     * @param accountId 账户ip
     * @param fileImg   文件数组
     * @return 图片访问路径列表
     */
    public Result<List<String>> uploadMultiImage(int accountId, MultipartFile[] fileImg) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("uploadMultiImage, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //上传的文件为空
        if (fileImg == null) {
            logger.warn("uploadMultiImage, fileImg is null");
            return new Result<>(ReturnCode.UPLOAD_FILE_IS_NULL);
        }
        List<Map<String, Object>> imageDbList = new ArrayList<>();
        List<String> successAccessPaths = new ArrayList<>();
        for (MultipartFile file : fileImg) {
            String fileName = file.getOriginalFilename();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            String[] names = fileName.split("\\.");
            byte[] fileBytes;
            try {
                fileBytes = file.getBytes();
            } catch (IOException e) {
                logger.error("uploadMultiImage, accountId: {}, fileName: {}, IOException: {}", accountId, fileName, e);
                continue;
            }

            //文件名称及格式合法性判断
            if (names.length > 0 && fileName.length() >= ConstValue.IMAGE_NAME_MIN_LEN
                    && ConstValue.IMAGE_SUPPORT_PATTERN.contains(names[names.length - 1]) && this.isFileSizeLegal(fileBytes.length)) {
                String accessPath;
                String storePath;
                String fileMd5 = DigestUtils.md5Hex(Base64.encodeBase64(fileBytes));
                List<Map<String, Object>> myImageDbList = laboDoService.queryImageListByHash(fileMd5, accountId);
                if (myImageDbList.isEmpty()) {
                    List<Map<String, Object>> dbImageInfoList = laboDoService.queryImageListByHash(fileMd5, null);
                    if (!dbImageInfoList.isEmpty()) {
                        Map<String, Object> dbImageInfo = dbImageInfoList.get(0);
                        accessPath = dbImageInfo.get("access_path").toString();
                        storePath = dbImageInfo.get("store_path").toString();
                    } else {
                        storePath = otherConfig.getBaseStorePath() + "/" + fileMd5;
                        File rootPathDir = new File(storePath);
                        if (!rootPathDir.exists()) {
                            logger.info("rootPathDir is created: {}, status: {}, accountId: {}, fileName: {}", rootPathDir, rootPathDir.mkdirs(), accountId, fileName);
                        }
                        String fileStorePath = rootPathDir.getPath() + "/" + fileName;
                        File imageFile = new File(fileStorePath);
                        try {

                            //使用工具类
                            FileUtils.copyInputStreamToFile(file.getInputStream(), imageFile);

                            //不使用工具类
                            //FileOutputStream outputStream = new FileOutputStream(imageFile);
                            //outputStream.write(fileBytes);
                            //outputStream.flush();
                            //outputStream.close();
                            logger.info("file store success, accountId: {}, fileName: {}", accountId, fileName);
                        } catch (IOException e) {
                            logger.error("accountId: {}, fileName: {}, file IOException: {}", accountId, fileName, e);
                            continue;
                        }
                        accessPath = otherConfig.getBaseAccessPath() + "/" + fileMd5 + "/" + fileName;
                    }

                    //入库文件名去除后缀
                    String imageFileName = fileName.substring(0, fileName.lastIndexOf("."));
                    HashMap<String, Object> imageInfo = new HashMap<>();
                    imageInfo.put("fileName", imageFileName);
                    imageInfo.put("fileHash", fileMd5);
                    imageInfo.put("storePath", storePath);
                    imageInfo.put("accessPath", accessPath);
                    imageDbList.add(imageInfo);
                    successAccessPaths.add(accessPath);
                } else {
                    successAccessPaths.add(myImageDbList.get(0).get("access_path").toString());
                }
            }
        }
        if (!imageDbList.isEmpty()) {
            laboDoService.addMultiImage(accountId, imageDbList);
        }
        return new Result<>(ReturnCode.SUCCESS, successAccessPaths);
    }

    /**
     * 获取其他项目列表
     *
     * @param accountId   账户Id
     * @param projectType 项目类型，0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站
     * @param currentPage 当前页，首页为1
     * @param pageSize    每页记录数量
     * @param totalNum    查询的记录总数
     */
    public Result<Map<String, Object>> getOtherProjectInfo(int accountId, int projectType, int currentPage, int pageSize, int totalNum) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("getOtherProjectInfo, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        List<OtherProjectInfoRes> dataList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();

        //多终端登录查询，数据记录可能增多；采取如下方案，查询第一条记录时，即确定要查询的总页数，保证数据正确性；适用于数据记录只增不减的情况
        Integer nowCount = laboDoService.queryProjectInfoCount(projectType);
        int totalCount = 0;
        if (nowCount != 0) {
            int offset;
            if (currentPage == 1) {  //首页，总记录数为数据库当前记录数
                totalCount = nowCount;
                offset = 0;
            } else { //非首页的总记录数使用前端回传参数
                totalCount = totalNum;
                if (nowCount < totalNum) {
                    offset = 0;
                    logger.warn("getOtherProjectInfo, request param: the value of totalNum is wrong or database record is lack, nowCount: {}, totalNum: {}, accountId: {}", nowCount, totalNum, accountId);
                } else {
                    offset = nowCount - totalNum + pageSize * (currentPage - 1);
                }
            }
            List<Map<String, Object>> recordList = laboDoService.queryProjectInfoList(projectType, pageSize, offset);
            for (Map<String, Object> eachRecord : recordList) {
                OtherProjectInfoRes eachRecordRes = new OtherProjectInfoRes();
                eachRecordRes.setId(Integer.parseInt(eachRecord.get("id").toString()));
                eachRecordRes.setName(eachRecord.get("project_name").toString());
                eachRecordRes.setUrl(eachRecord.get("project_url").toString());
                eachRecordRes.setImage((String) eachRecord.get("project_image"));
                eachRecordRes.setDes((String) eachRecord.get("project_des"));
                dataList.add(eachRecordRes);
            }
        }
        data.put("totalCount", totalCount);
        data.put("dataList", dataList);
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    /**
     * 新增项目记录
     *
     * @param accountId 账户Id
     * @param type      项目类型，0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站
     * @param name      项目名称
     * @param url       访问链接
     * @param image     缩略图url
     * @param des       项目描述
     */
    public Result<String> addOtherProjectInfo(int accountId, int type, String name, String url, String image, String des) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("addOtherProjectInfo, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //校验是否有管理员权限
        boolean isAdmin = Boolean.parseBoolean(accountInfo.get("is_admin").toString());
        if (!isAdmin) {
            logger.warn("addOtherProjectInfo, account isn't admin, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_NOT_ADMIN);
        }

        //入库
        laboDoService.addProjectInfo(type, name, url, image, des);
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 删除项目记录
     *
     * @param accountId 账户Id
     * @param id        记录标识
     */
    public Result<String> deleteOtherProjectInfo(int accountId, int id) {
        Map<String, Object> accountInfo = laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            logger.warn("deleteOtherProjectInfo, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //校验是否有管理员权限
        boolean isAdmin = Boolean.parseBoolean(accountInfo.get("is_admin").toString());
        if (!isAdmin) {
            logger.warn("deleteOtherProjectInfo, account isn't admin, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_NOT_ADMIN);
        }
        laboDoService.removeProjectInfoById(id);
        return new Result<>(ReturnCode.SUCCESS);
    }

}
