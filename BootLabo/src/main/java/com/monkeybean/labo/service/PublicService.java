package com.monkeybean.labo.service;

import com.monkeybean.labo.service.database.LaboDoService;
import com.monkeybean.labo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by MonkeyBean on 2018/7/5.
 */
@Service
public class PublicService {

    private static Logger logger = LoggerFactory.getLogger(PublicService.class);

    private final LaboDoService laboDoService;

    @Autowired
    public PublicService(LaboDoService laboDoService) {
        this.laboDoService = laboDoService;
    }

    /**
     * 查询账号是否合法(存在且未被封号)
     *
     * @param accountInfo 账户信息
     * @return 合法true, 非法返回false
     */
    boolean checkAccountLegal(HashMap<String, Object> accountInfo) {
        if (accountInfo != null) {
//            boolean hasForbidden = DateUtil.checkNowBeforeDate((Date)accountInfo.get("forbid_time"));
            boolean hasForbidden = DateUtil.checkNowBeforeDate(accountInfo.get("forbidTime_format").toString());
            if (!hasForbidden) {
                return true;
            }
        }
        return false;
    }
}
