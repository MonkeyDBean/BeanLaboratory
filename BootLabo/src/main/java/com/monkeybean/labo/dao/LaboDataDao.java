package com.monkeybean.labo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@Mapper
public interface LaboDataDao {

    /**
     * account表, select
     */
    HashMap<String, Object> queryAccountInfo(HashMap<String, Object> param);

    Integer queryMaxAccountId();

    /**
     * 测试，使用mybatis for each
     */
    List<HashMap<String, Object>> queryListByArray(HashMap<String, Object> param);

    /**
     * account表, update
     */
    void updateAccountInfo(HashMap<String, Object> param);

    /**
     * account表, insert
     */
    void addAccountInfo(HashMap<String, Object> param);

    /**
     * message_record表, select
     */
    HashMap<String, Object> queryLatestMessageRecord(HashMap<String, Object> param);

    Integer queryTodayMessageApplyCount(@Param("phone") String phone);

    /**
     * message_record表, update
     */
    void updateMessageRecord(@Param("phone") String phone);

    /**
     * message_record表, insert
     */
    void addMessageRecord(HashMap<String, Object> param);

    /**
     * message_record表, delete
     */

    /**
     * asset_temp表, select
     */
    HashMap<String, Object> queryTempAsset(@Param("fileName") String fileName);

    /**
     * asset_temp表, insert
     */
    void addNewTempAsset(HashMap<String, Object> param);

    /**
     * asset_temp表, delete
     */
    void clearTempAssetBeforeDate(@Param("fileName") String dateStr);

    /**
     * image_info表, query
     */
    List<HashMap<String, Object>> queryImageInfoList(HashMap<String, Object> param);

    Integer queryImageInfoCount(HashMap<String, Object> param);

    /**
     * image_info表，update
     */
    void updateImageInfo(HashMap<String, Object> param);

    void updateImageListStatus(HashMap<String, Object> param);

    /**
     * image_info表，insert
     */
    void addImageInfo(HashMap<String, Object> param);

    void addMultiImage(HashMap<String, Object> param);

    /**
     * other_project_info表，query
     */
    List<HashMap<String, Object>> queryProjectInfoList(HashMap<String, Object> param);

    Integer queryProjectInfoCount(@Param("projectType") Integer projectType);

    /**
     * config_info表，query
     */
    String queryConfigValue(@Param("configName") String configName);

    /**
     * config_info表，update
     */
    void updateConfigValue(HashMap<String, Object> param);

    /**
     * config_info表，insert
     */
    void addConfigInfo(HashMap<String, Object> param);

}
