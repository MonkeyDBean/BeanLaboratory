package com.monkeybean.labo.service;

import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2018/7/5.
 */
public interface PublicService {

    boolean checkAccountLegal(Map<String, Object> accountInfo);

    int getNewAccountId();

    List<Integer> getNowIds();

}
