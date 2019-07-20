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
import java.util.concurrent.ConcurrentHashMap;

/**
 * 阿里云短信服务
 * 接口文档地址：https://help.aliyun.com/document_detail/55284.html?spm=5176.10629532.106.1.31791cberqSoem
 * <p>
 * Created by MonkeyBean on 2018/05/19.
 */
public final class AliYunUtil {

    /**
     * 阿里云申请短信, 返回码集合
     * key为response code ; 若为严重级别(服务不可用),value为true
     * 错误码参考：https://help.aliyun.com/document_detail/55284.html?spm=5176.10629532.106.1.31791cberqSoem
     * 解决方法参考：https://help.aliyun.com/knowledge_detail/57717.html?spm=a2c4g.11186623.2.9.PreCc6
     */
    public static final ConcurrentHashMap<String, Boolean> responseCodeMap = new ConcurrentHashMap<String, Boolean>() {
        {
            //请求成功
            put("OK", false);

            //RAM权限DENY
            put("isp.RAM_PERMISSION_DENY", true);

            //业务停机: 请先查看账户余额，若余额大于零，则请通过创建工单联系工程师处理
            put("isv.OUT_OF_SERVICE", true);

            //未开通云通信产品的阿里云客户
            put("isv.PRODUCT_UN_SUBSCRIPT", true);

            //产品未开通
            put("isv.PRODUCT_UNSUBSCRIBE", true);

            //账户不存在
            put("isv.ACCOUNT_NOT_EXISTS", true);

            //账户异常
            put("isv.ACCOUNT_ABNORMAL", true);

            //短信模板不合法
            put("isv.SMS_TEMPLATE_ILLEGAL", true);

            //短信签名不合法
            put("isv.SMS_SIGNATURE_ILLEGAL", true);

            //参数异常
            put("isv.INVALID_PARAMETERS", true);

            //系统错误
            put("isv.SYSTEM_ERROR", true);

            //非法手机号
            put("isv.MOBILE_NUMBER_ILLEGAL", false);

            //手机号码数量超过限制
            put("isv.MOBILE_COUNT_OVER_LIMIT", true);

            //模板缺少变量
            put("isv.TEMPLATE_MISSING_PARAMETERS", true);

            //业务限流
            put("isv.BUSINESS_LIMIT_CONTROL", false);

            //JSON参数不合法，只接受字符串值
            put("isv.INVALID_JSON_PARAM", true);

            //黑名单管控
            put("isv.BLACK_KEY_CONTROL_LIMIT", true);

            //参数超出长度限制
            put("isv.PARAM_LENGTH_LIMIT", true);

        }
    };

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
