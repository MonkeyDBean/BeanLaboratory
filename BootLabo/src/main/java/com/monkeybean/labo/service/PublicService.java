package com.monkeybean.labo.service;

import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.service.database.LaboDoService;
import com.monkeybean.labo.util.CommonUtil;
import com.monkeybean.labo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by MonkeyBean on 2018/7/5.
 */
@Service
public class PublicService {

    private static Logger logger = LoggerFactory.getLogger(PublicService.class);

    /**
     * 存储预先生成的Id列表
     */
    private LinkedList<Integer> accountIds = new LinkedList<>();

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

    /**
     * 验证账户Id规则
     * 一定区间内的账户Id, 个、十、百位数字之和不能等于特定值，尾数不能等于特定值，且不能被100整除
     *
     * @param accountId 账户Id
     * @return 通过则返回true
     */
    private boolean conformIdRule(int accountId) {
        int tail = CommonUtil.getNum(accountId, 0);
        int sum = tail + CommonUtil.getNum(accountId, 1) + CommonUtil.getNum(accountId, 2);
        if (accountId >= 100 && accountId <= 100000) {
            if (ConstValue.reservedIdTail.contains(tail) || ConstValue.reservedIdSum.contains(sum)) {
                return false;
            }
            if (accountId % 100 == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 生成不连续的账户ID列表
     */
    private void initRandomAccountIds() {

        //已使用的id最大上限
        Integer idBaseSeed = laboDoService.queryIdBaseSeed();
        if (idBaseSeed == null) {
            idBaseSeed = laboDoService.queryMaxAccountId();
            if (idBaseSeed == null) {
                idBaseSeed = ConstValue.ID_START;
            }
            laboDoService.addIdBaseSeed(String.valueOf(idBaseSeed));
        }

        //生成新Id的区间范围
        int randomCount = 100;

        //生成新Id的最大数量
        int threshold = 10;

        //id增长幅度
        int scale = 3;
        int increaseNum = Math.floorDiv(randomCount - 1, scale);

        //存储临时Id列表
        List<Integer> temp = new ArrayList<>(increaseNum);
        for (int i = 1; i <= increaseNum; i++) {
            temp.add(idBaseSeed + scale * i);
        }
        Collections.shuffle(temp);
        accountIds.clear();
        for (int accountId : temp) {
            if (accountIds.size() >= threshold) {
                break;
            }
            if (conformIdRule(accountId)) {
                accountIds.add(accountId);
            }
        }

        //新id最大上限
        int newIdBaseSeed = idBaseSeed + randomCount;
        laboDoService.updateIdBaseSeed(newIdBaseSeed);
    }

    /**
     * 获取新账户Id
     */
    public synchronized int getNewAccountId() {
        while (accountIds.isEmpty()) {
            initRandomAccountIds();
        }
        return accountIds.pop();
    }

    /**
     * 获取当前使用的Id列表
     */
    public List<Integer> getNowIds() {
        while (accountIds.isEmpty()) {
            initRandomAccountIds();
        }
        return accountIds;
    }
}
