package com.monkeybean.labo.service.database;

import com.monkeybean.labo.dao.LaboDataDao;
import com.monkeybean.labo.predefine.ConstValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
@Service
public class LaboDoService {

    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_ACCOUNT_ID = "accountId";
    private static final String PARAM_FILE_NAME = "fileName";
    private static final String PARAM_IS_SHARE = "isShare";
    private static final String PARAM_ID_LIST = "idList";
    private static Logger logger = LoggerFactory.getLogger(LaboDoService.class);
    private final LaboDataDao laboDao;

    /**
     * 存储所有方法，key为方法名，value为Method
     */
    private final ConcurrentHashMap<String, Method> daoMethod = new ConcurrentHashMap<>();

    @Autowired
    public LaboDoService(LaboDataDao laboDao) {
        this.laboDao = laboDao;
        for (Method method : laboDao.getClass().getDeclaredMethods()) {
            this.daoMethod.put(method.getName(), method);
        }
    }

    /**
     * 测试反射机制调用方法
     *
     * @param params     请求参数
     * @param methodName 方法名称
     * @return 信息列表
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> testReflectionInvoke(HashMap<String, Object> params, String methodName) {
        List<HashMap<String, Object>> result = null;
        try {
            result = (List<HashMap<String, Object>>) this.daoMethod.get(methodName).invoke(laboDao, params);
        } catch (Exception e) {
            logger.error("testReflectionInvoke, method reflect IllegalAccessException or InvocationTargetException: {}", e);
        }
        return result;
    }

    /**
     * 通过手机号查账户信息
     *
     * @param phone 账户手机号
     * @return 账户信息
     */
    public HashMap<String, Object> queryAccountInfoByPhone(String phone) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_PHONE, phone);
        return laboDao.queryAccountInfo(param);
    }

    /**
     * 通过邮箱查账户信息
     *
     * @param email 账户邮箱
     * @return 账户信息
     */
    public HashMap<String, Object> queryAccountInfoByEmail(String email) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("email", email);
        return laboDao.queryAccountInfo(param);
    }

    /**
     * 通过账户Id查账户信息
     *
     * @param accountId 账户Id
     * @return 账户信息
     */
    public HashMap<String, Object> queryAccountInfoById(Integer accountId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        return laboDao.queryAccountInfo(param);
    }

    /**
     * 查询最大账户Id
     */
    public Integer queryMaxAccountId() {
        Integer maxId = laboDao.queryMaxAccountId();
        return maxId != null ? maxId : ConstValue.ID_START;
    }

    /**
     * 通过手机号更新账户信息
     */
    private void updateAccountInfoByPhone(String phone, HashMap<String, Object> param) {
        param.put("phoneParam", phone);
        laboDao.updateAccountInfo(param);
    }

    /**
     * 通过账户id更新账户信息
     */
    private void updateAccountInfoById(Integer accountId, HashMap<String, Object> param) {
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        laboDao.updateAccountInfo(param);
    }

    /**
     * 更新账户登录信息
     *
     * @param ipv4      ip地址
     * @param accountId 账户Id
     */
    public void updateLoginInfoById(String ipv4, Integer accountId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("ipv4", ipv4);
        param.put("loginTime", new Timestamp(System.currentTimeMillis()));
        updateAccountInfoById(accountId, param);
    }

    /**
     * 更新账户头像
     */
    public void updateAvatar(Integer accountId, byte[] avatar) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("avatar", avatar);
        updateAccountInfoById(accountId, param);
    }

    /**
     * 更新账户密码
     */
    public void updatePasswordById(Integer accountId, String password) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("password", password);
        updateAccountInfoById(accountId, param);
    }

    /**
     * 更新邮箱
     */
    public void updateEmail(String phone, String email) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("email", email);
        updateAccountInfoByPhone(phone, param);
    }

    /**
     * 新增账户
     *
     * @param accountId 账户Id
     * @param phone     手机号
     * @param password  des加密后的密码
     * @param nickname  昵称
     * @param ipv4      ip地址
     */
    public void addAccountInfo(Integer accountId, String phone, String password, String nickname, String ipv4) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put(LaboDoService.PARAM_PHONE, phone);
        param.put("password", password);
        param.put("nickname", nickname);
        param.put("ipv4", ipv4);
        laboDao.addAccountInfo(param);
    }

    /**
     * 通过手机号和验证状态查询短信申请记录
     *
     * @param phone        手机号
     * @param verifyStatus 验证码是否已使用；传null则不使用该参数查询数据
     */
    public HashMap<String, Object> queryLatestMessageRecord(String phone, Boolean verifyStatus) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_PHONE, phone);
        param.put("verifyStatus", verifyStatus);
        return laboDao.queryLatestMessageRecord(param);
    }

    /**
     * 查询今日特定手机号短信申请次数
     *
     * @param phone 手机号
     * @return 申请次数
     */
    public Integer queryTodayMessageApplyCount(String phone) {
        return laboDao.queryTodayMessageApplyCount(phone);
    }

    /**
     * 更新短信验证状态
     *
     * @param phone 手机号
     */
    public void updateMessageStatus(String phone) {
        laboDao.updateMessageRecord(phone);
    }

    /**
     * 新增短信记录
     */
    public void addMessageRecord(String phone, String messageCode, String resCode, String resCustom) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_PHONE, phone);
        param.put("messageCode", messageCode);
        param.put("resCode", resCode);
        param.put("resCustom", resCustom);
        laboDao.addMessageRecord(param);
    }

    /**
     * 查询临时图片
     */
    public Map<String, Object> queryTempAsset(String fileName) {
        return laboDao.queryTempAsset(fileName);
    }

    /**
     * 新增临时图片
     */
    public void addNewTempAsset(Integer accountId, String fileName, byte[] cover) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put(LaboDoService.PARAM_FILE_NAME, fileName);
        param.put("cover", cover);
        laboDao.addNewTempAsset(param);
    }

    /**
     * 清除历史临时图片
     */
    public void clearTempAssetBeforeDate(String dateStr) {
        laboDao.clearTempAssetBeforeDate(dateStr);
    }

    /**
     * 按图片Id查询图片信息
     *
     * @param imageId   图片Id
     * @param accountId 账户Id
     */
    public List<HashMap<String, Object>> queryImageById(Integer imageId, Integer accountId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("id", imageId);
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        return laboDao.queryImageInfoList(param);
    }

    /**
     * 通过hash值查图片信息列表
     *
     * @param fileHash  文件hash
     * @param accountId 账户Id,传null则不按账户Id查询
     */
    public List<HashMap<String, Object>> queryImageListByHash(String fileHash, Integer accountId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("fileHash", fileHash);
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        return laboDao.queryImageInfoList(param);
    }

    /**
     * 按访问权限类型查询图片信息列表
     *
     * @param isShare   是否共享, 0为仅私有图片，1为共享图片，null为查询所有图片
     * @param accountId 账户Id, 传null则不按账户Id查询; isShare为1时，accountId传null
     * @param limit     每一页的记录数
     * @param offset    查询记录的起始位，记录偏移量
     */
    public List<HashMap<String, Object>> queryImageListByShareType(Integer isShare, Integer accountId, Integer limit, Integer offset) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_IS_SHARE, isShare);
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put("limit", limit);
        param.put("offset", offset);
        return laboDao.queryImageInfoList(param);
    }

    /**
     * 按访问权限类型查图片记录总数
     */
    public Integer queryImageCountByShareType(Integer isShare, Integer accountId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_IS_SHARE, isShare);
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        return laboDao.queryImageInfoCount(param);
    }

    /**
     * 查询图片列表的共享类型
     *
     * @param idList    图片Id列表
     * @param accountId 账户Id
     */
    public List<HashMap<String, Object>> queryImageShareStatusList(List<Integer> idList, Integer accountId) {
        if (!idList.isEmpty()) {
            HashMap<String, Object> param = new HashMap<>();
            param.put(LaboDoService.PARAM_ID_LIST, idList);
            param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
            return laboDao.queryImageShareStatusList(param);
        }
        return new ArrayList<>();
    }

    /**
     * 更新图片信息
     *
     * @param accountId 账户Id
     * @param fileName  文件名
     * @param fileDes   文件描述
     */
    public void updateImageInfo(Integer accountId, String fileName, String fileDes) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put(LaboDoService.PARAM_FILE_NAME, fileName);
        param.put("fileDes", fileDes);
        laboDao.updateImageInfo(param);
    }

    /**
     * 移除图片
     *
     * @param idList    图片Id列表
     * @param accountId 账户Id
     */
    public void removeImages(List<Integer> idList, Integer accountId) {
        if (!idList.isEmpty()) {
            HashMap<String, Object> param = new HashMap<>();
            param.put(LaboDoService.PARAM_ID_LIST, idList);
            param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
            param.put("isDelete", 1);
            laboDao.updateImageListStatus(param);
        }
    }

    /**
     * 更改图片共享状态
     *
     * @param idList    图片Id列表
     * @param accountId 账户Id
     * @param isShare   是否共享，1为共享，0为私有
     */
    public void changeImageShareStatus(List<Integer> idList, Integer accountId, Integer isShare) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ID_LIST, idList);
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put(LaboDoService.PARAM_IS_SHARE, isShare);
        laboDao.updateImageListStatus(param);
    }

    /**
     * 单张图片入库
     *
     * @param accountId  账户Id
     * @param fileName   文件名
     * @param fileHash   图片md5 32位Hex字符创
     * @param storePath  存储路径
     * @param accessPath 访问路径
     */
    public void addImageInfo(Integer accountId, String fileName, String fileHash, String storePath, String accessPath) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
        param.put(LaboDoService.PARAM_FILE_NAME, fileName);
        param.put("fileHash", fileHash);
        param.put("storePath", storePath);
        param.put("accessPath", accessPath);
        laboDao.addImageInfo(param);
    }

    /**
     * 多张图片入库
     */
    public void addMultiImage(Integer accountId, List<HashMap<String, Object>> imageList) {
        if (!imageList.isEmpty()) {
            HashMap<String, Object> param = new HashMap<>();
            param.put(LaboDoService.PARAM_ACCOUNT_ID, accountId);
            param.put("imageList", imageList);
            laboDao.addMultiImage(param);
        }
    }

    /**
     * 查询项目展示信息列表
     *
     * @param projectType 项目类型：0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站
     * @param limit       每一页的记录数
     * @param offset      查询记录的起始位，记录偏移量
     */
    public List<HashMap<String, Object>> queryProjectInfoList(Integer projectType, Integer limit, Integer offset) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("projectType", projectType);
        param.put("limit", limit);
        param.put("offset", offset);
        return laboDao.queryProjectInfoList(param);
    }

    /**
     * 查询项目展示记录总数
     *
     * @param projectType 项目类型：0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站
     */
    public Integer queryProjectInfoCount(Integer projectType) {
        return laboDao.queryProjectInfoCount(projectType);
    }

    /**
     * 查询账户Id当前种子基数
     *
     * @return 不存在则返回null
     */
    public Integer queryIdBaseSeed() {
        String configValue = laboDao.queryConfigValue(ConstValue.idBaseName);
        return configValue != null ? Integer.valueOf(configValue) : null;
    }

    /**
     * 更新账户Id的种子基数
     *
     * @param idSeed 种子基数
     */
    public void updateIdBaseSeed(Integer idSeed) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("configName", ConstValue.idBaseName);
        param.put("configValue", String.valueOf(idSeed));
        laboDao.updateConfigValue(param);
    }

    /**
     * 新增账户Id的种子基数配置
     */
    public void addIdBaseSeed(String configValue) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("configName", ConstValue.idBaseName);
        param.put("configValue", configValue);
        laboDao.addConfigInfo(param);
    }

}
