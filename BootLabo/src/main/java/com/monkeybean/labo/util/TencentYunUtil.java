package com.monkeybean.labo.util;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.monkeybean.labo.predefine.ConstValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯云短信服务
 * 接口文档地址：https://github.com/qcloudsms/qcloudsms_java
 * https://cloud.tencent.com/document/product/382/5976#.E6.8E.A5.E5.8F.A3.E6.8F.8F.E8.BF.B0
 * <p>
 * Created by MonkeyBean on 2018/05/19.
 */
public class TencentYunUtil {

    private static Logger logger = LoggerFactory.getLogger(TencentYunUtil.class);

    /**
     * @param appId      短信应用SDK AppID
     * @param appKey     短信应用SDK AppKey
     * @param smsSign    签名
     * @param templateId 模板ID
     * @param phone      手机号
     * @param code       验证码
     * @return 接口的调用状态
     */
    public static Map<String, Object> sendMessage(Integer appId, String appKey, String smsSign, Integer templateId, String phone, String code) {
        Map<String, Object> result = new HashMap<>();
        SmsSingleSender smsSingleSender = new SmsSingleSender(appId, appKey);
        String[] params = {code};
        SmsSingleSenderResult smsSingleSenderResult;
        try {

            // 签名参数未提供或者为空时，会使用默认签名发送短信
            smsSingleSenderResult = smsSingleSender.sendWithParam("86", phone, templateId, params, smsSign, "", "");
        } catch (HTTPException | IOException e) {
            logger.error("http response code error or tencent cloud io error: {}", e);
            result.put("result", ConstValue.SEND_FAIL);
            return result;
        }
        logger.info("sendResult is : {}, errMsg is: {}, ext is: {}, fee is: {}, sid is: {}",
                smsSingleSenderResult.result, smsSingleSenderResult.errMsg, smsSingleSenderResult.ext, smsSingleSenderResult.fee, smsSingleSenderResult.sid);
        result.put("result", ConstValue.SEND_SUCCESS);
        result.put("response", smsSingleSenderResult.result);
        return result;
    }

}
