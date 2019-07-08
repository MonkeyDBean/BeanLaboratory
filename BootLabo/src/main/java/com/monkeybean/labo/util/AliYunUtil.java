package com.monkeybean.labo.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.monkeybean.labo.predefine.ConstValue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云短信服务
 * 接口文档地址：https://help.aliyun.com/document_detail/55284.html?spm=5176.10629532.106.1.31791cberqSoem
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class AliYunUtil {
    /**
     * 云通信短信API产品及产品域名,开发者无需替换
     */
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String END_POINT_AREA = "cn-beijing";
    private static final String TIMEOUT_STR = "10000";
    private static Logger logger = LoggerFactory.getLogger(AliYunUtil.class);

    private AliYunUtil() {
    }

    /**
     * 短信验证码接口
     * 使用同一个签名，对同一个手机号码发送短信验证码，1条/分钟，5条/小时
     *
     * @param accessKeyId     访问密钥Id
     * @param accessKeySecret 访问密钥key
     * @param signName        签名
     * @param templateCode    模板Id
     * @param phone           手机号
     * @param code            验证码
     * @return 接口的调用状态
     */
    public static Map<String, Object> sendMessage(String accessKeyId, String accessKeySecret, String signName, String templateCode, String phone, String code) {
        HashMap<String, Object> result = new HashMap<>();
        final String sendResultKey = "result";

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", AliYunUtil.TIMEOUT_STR);
        System.setProperty("sun.net.client.defaultReadTimeout", AliYunUtil.TIMEOUT_STR);

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(AliYunUtil.END_POINT_AREA, accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint(AliYunUtil.END_POINT_AREA, AliYunUtil.END_POINT_AREA, AliYunUtil.PRODUCT, AliYunUtil.DOMAIN);
        } catch (ClientException e) {
            logger.error("ali yun sendMessage, ClientException: [{}]", e);
            result.put(sendResultKey, ConstValue.SEND_FAIL);
            return result;
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            logger.error("ali yun sendMessage, ClientException: [{}]", e);
            result.put(sendResultKey, ConstValue.SEND_FAIL);
            return result;
        }
        result.put(sendResultKey, ConstValue.SEND_SUCCESS);
        result.put("response", sendSmsResponse);
        return result;
    }

    /**
     * 查询短信明细返回数据
     *
     * @param accessKeyId     访问密钥Id
     * @param accessKeySecret 访问密钥key
     * @param bizId           发送回执Id
     * @param phone           手机号
     * @return QuerySendDetailsResponse
     * @throws ClientException acsClient异常
     */
    public static QuerySendDetailsResponse querySendDetails(String accessKeyId, String accessKeySecret, String phone, String bizId) throws ClientException {

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", AliYunUtil.TIMEOUT_STR);
        System.setProperty("sun.net.client.defaultReadTimeout", AliYunUtil.TIMEOUT_STR);

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(AliYunUtil.END_POINT_AREA, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(AliYunUtil.END_POINT_AREA, AliYunUtil.END_POINT_AREA, AliYunUtil.PRODUCT, AliYunUtil.DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phone);
        //可选-流水号
        request.setBizId(bizId);

        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        //SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        //request.setSendDate(ft.format(new Date()));

        // joda DateTime线程安全，SimpleDateFormat线程不安全
        request.setSendDate(new DateTime(System.currentTimeMillis()).toString("yyyyMMdd"));

        //必填-页大小
        request.setPageSize(10L);

        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        return acsClient.getAcsResponse(request);
    }

    /**
     * 输出短信明细
     */
    public static Map<String, Object> printSendDetails(QuerySendDetailsResponse querySendDetailsResponse) {
        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> recordList = new ArrayList<>();
        for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
            HashMap<String, Object> record = new HashMap<>();
            record.put("Content", smsSendDetailDTO.getContent());
            record.put("ErrCode", smsSendDetailDTO.getErrCode());
            record.put("OutId", smsSendDetailDTO.getOutId());
            record.put("PhoneNum", smsSendDetailDTO.getPhoneNum());
            record.put("ReceiveDate", smsSendDetailDTO.getReceiveDate());
            record.put("SendDate", smsSendDetailDTO.getSendDate());
            record.put("SendStatus", smsSendDetailDTO.getSendStatus());
            record.put("Template", smsSendDetailDTO.getTemplateCode());
            recordList.add(record);
        }
        result.put("code", querySendDetailsResponse.getCode());
        result.put("message", querySendDetailsResponse.getMessage());
        result.put("recordList", recordList);
        result.put("TotalCount", querySendDetailsResponse.getTotalCount());
        result.put("RequestId", querySendDetailsResponse.getRequestId());
        return result;
    }

}
