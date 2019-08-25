package com.monkeybean.labo.service.database;

import com.monkeybean.labo.dao.secondary.SecondaryDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MonkeyBean on 2019/07/28.
 */
@Service
public class SecondaryDoService {
    private static Logger logger = LoggerFactory.getLogger(SecondaryDoService.class);
    private final SecondaryDataDao secondaryDataDao;

    @Autowired
    public SecondaryDoService(SecondaryDataDao secondaryDataDao) {
        this.secondaryDataDao = secondaryDataDao;
    }

    /**
     * 查询测试记录
     *
     * @param id 主键Id
     */
    public Map<String, Object> queryTestRecord(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return secondaryDataDao.queryTestRecord(param);
    }

    /**
     * 通过长链接查询长短链映射记录
     *
     * @param longUrl 长链接
     */
    public Map<String, Object> querySLRecordByLongUrl(String longUrl) {
        Map<String, Object> param = new HashMap<>();
        param.put("longUrl", longUrl);
        return secondaryDataDao.queryShortLongRecord(param);
    }

    /**
     * 通过短链接标识查询长短链映射记录
     *
     * @param shortFlag 短链接标识
     */
    public Map<String, Object> querySLRecordByShortFlag(String shortFlag) {
        Map<String, Object> param = new HashMap<>();
        param.put("shortFlag", shortFlag);
        return secondaryDataDao.queryShortLongRecord(param);
    }

    /**
     * 插入长短链映射记录
     *
     * @param longUrl   长链接
     * @param shortUrl  短链接
     * @param shortFlag 短链接标识
     */
    public void addShortLongRecord(String longUrl, String shortUrl, String shortFlag) {
        Map<String, Object> param = new HashMap<>();
        param.put("longUrl", longUrl);
        param.put("shortUrl", shortUrl);
        param.put("shortFlag", shortFlag);
        secondaryDataDao.addShortLongRecord(param);
    }

    @Transactional
    public void testTransaction(String longUrl, String shortUrl, String shortFlag) {
        addShortLongRecord(longUrl, shortUrl, shortFlag);
        if (querySLRecordByShortFlag(shortFlag) != null) {
            logger.info("testTransaction, querySLRecordByShortFlag is not null shortFlag: [{}]", shortFlag);
        }
    }

}
