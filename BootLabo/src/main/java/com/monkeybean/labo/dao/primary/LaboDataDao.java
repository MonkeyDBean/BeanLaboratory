package com.monkeybean.labo.dao.primary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@Mapper
public interface LaboDataDao {

    /**
     * account表, select
     */
    Map<String, Object> queryAccountInfo(Map<String, Object> param);

    Integer queryMaxAccountId();

    /**
     * 测试，使用mybatis for each
     */
    List<Map<String, Object>> queryListByArray(Map<String, Object> param);

    /**
     * account表, update
     */
    void updateAccountInfo(Map<String, Object> param);

    /**
     * account表, insert
     */
    void addAccountInfo(Map<String, Object> param);

    /**
     * message_record表, select
     */
    Map<String, Object> queryLatestMessageRecord(Map<String, Object> param);

    Integer queryTodayMessageApplyCount(@Param("phone") String phone);

    /**
     * message_record表, update
     */
    void updateMessageRecord(@Param("phone") String phone);

    /**
     * message_record表, insert
     */
    void addMessageRecord(Map<String, Object> param);

    /**
     * asset_temp表, select
     */
    Map<String, Object> queryTempAsset(@Param("fileName") String fileName);

    /**
     * asset_temp表, insert
     */
    void addNewTempAsset(Map<String, Object> param);

    /**
     * asset_temp表, delete
     */
    void clearTempAssetBeforeDate(@Param("dateStr") String dateStr);

    /**
     * image_info表, query
     */
    List<Map<String, Object>> queryImageInfoList(Map<String, Object> param);

    Integer queryImageInfoCount(Map<String, Object> param);

    List<Map<String, Object>> queryImageShareStatusList(Map<String, Object> param);

    /**
     * image_info表，update
     */
    void updateImageInfo(Map<String, Object> param);

    void updateImageListStatus(Map<String, Object> param);

    /**
     * image_info表，insert
     */
    void addImageInfo(Map<String, Object> param);

    void addMultiImage(Map<String, Object> param);

    /**
     * other_project_info表，query
     */
    List<Map<String, Object>> queryProjectInfoList(Map<String, Object> param);

    Integer queryProjectInfoCount(@Param("projectType") Integer projectType);

    /**
     * other_project_info表，伪删除，update
     */
    void removeProjectInfoById(@Param("id") Integer id);

    /**
     * other_project_info表，insert
     */
    void addProjectInfo(Map<String, Object> param);

    /**
     * config_info表，query
     */
    String queryConfigValue(@Param("configName") String configName);

    /**
     * config_info表，update
     */
    void updateConfigValue(Map<String, Object> param);

    /**
     * config_info表，insert
     */
    void addConfigInfo(Map<String, Object> param);

}
