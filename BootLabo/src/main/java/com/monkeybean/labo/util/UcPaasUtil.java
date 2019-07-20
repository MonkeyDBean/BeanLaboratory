package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeybean.labo.predefine.ConstValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 云之讯短信服务
 * 云之讯短信接口文档地址：http://docs.ucpaas.com/doku.php?id=%E7%9F%AD%E4%BF%A1%E9%AA%8C%E8%AF%81:rest_yz_rest
 * <p>
 * Created by MonkeyBean on 2018/05/19.
 */
public class UcPaasUtil {

    /**
     * 云之讯申请短信, 返回码集合
     * key为response code; 若为严重级别(服务不可用), value为true
     * 错误码及解决方案参考：http://docs.ucpaas.com/doku.php?id=error_code
     */
    public static final ConcurrentHashMap<String, Boolean> responseCodeMap = new ConcurrentHashMap<String, Boolean>() {
        {
            //请求成功
            put("000000", false);

            //账户余额/套餐包余额不足
            put("100001", true);

            //发送请求的IP不在白名单内
            put("100005", false);

            //手机号码不能为空
            put("100008", false);

            //手机号为受保护的号码
            put("100009", false);

            //号码不合法
            put("100015", false);

            //账号余额被冻结
            put("100016", true);

            //余额已注销
            put("100017", true);

            //应用可用额度余额不足
            put("100019", true);

            //系统内部错误
            put("100699", true);

            //主账户sid存在非法字符
            put("101105", true);

            //开发者账户已注销
            put("101108", true);

            //主账户sid未激活
            put("101109", true);

            //主账户sid已锁定
            put("101110", true);

            //主账户sid不存在
            put("101111", true);

            //主账户sid为空
            put("101112", true);

            //缺少token参数或参数为空
            put("101117", true);

            //应用appid为空
            put("102100", true);

            //应用appid存在非法字符
            put("102101", true);

            //应用appid不存在
            put("102102", true);

            //应用未上线
            put("102103", true);

            //应用appid不属于该主账号
            put("102105", true);

            //未上线应用只能使用白名单中的号码
            put("103126", true);

            //该appid下，此短信模板(templateid)不存在
            put("105110", true);

            //短信模板(templateid)未审核通过
            put("105111", true);

            //请求的参数(param)与模板上的变量数量不一致
            put("105112", true);

            //短信模板(templateid)不能为空
            put("105113", true);

            //短信类型(type)长度应为1个字符
            put("105115", true);

            //短信模板(templateid)含非法字符
            put("105117", true);

            //短信模板有替换内容，但缺少param参数或参数为空
            put("105118", true);

            //每个参数的长度不能超过100字
            put("105119", true);

            //群发号码单次提交不能超过100个
            put("105120", true);

            //短信模板(templateid)已删除
            put("105121", true);

            //短信模板内容为空
            put("105124", true);

            //创建短信模板失败
            put("105125", true);

            //短信模板名称格式错误
            put("105126", true);

            //短信模板(templateid)不能为空
            put("105128", true);

            //短信内容过长，超过500字
            put("105133", true);

            //参数(param)中含有超过一对【】
            put("105134", true);

            //参数(param)中含有特殊符号
            put("105135", true);

            //签名长度应为2到12位
            put("105136", true);

            //群发号码重复
            put("105138", true);

            //	账号未认证
            put("105140", true);

            //主账号需为企业认证
            put("105141", true);

            //模板被定时群发任务锁定暂无法修改
            put("105142", true);

            //模板不属于该用户
            put("105143", true);

            //创建验证码模板短信需带参数
            put("105144", true);

            //签名(autograph)格式错误
            put("105145", true);

            //短信类型(type)错误
            put("105146", true);

            //对同个号码发送短信超过限定频率
            put("105147", false);

            //短信发送频率过快
            put("105150", false);

            //请求的参数(param)格式错误
            put("105152", true);

            //手机号码格式错误
            put("105153", false);

            //短信服务请求异常e100
            put("105154", true);

            //缺少签名(autograph)参数或参数为空
            put("105155", true);

            //查询短信类型错误
            put("105156", true);

            //变量数量超过100个
            put("105157", true);

            //接口不支持GET方式调用
            put("105158", true);

            //接口不支持POST方式调用
            put("105159", true);

            //开始时间错误
            put("105161", true);

            //结束时间错误
            put("105162", true);

            //超过可查询时间范围
            put("105163", false);

            //页码错误
            put("105164", false);

            //每页个数错误，限制访问(1-100)
            put("105165", false);

            //请求频率过快
            put("105166", false);

            //uid格式错误或超过60位
            put("105167", true);

            //参数sid或token错误
            put("105168", true);

            //超过页码数
            put("105169", false);

            //提交失败
            put("300001", true);

            //未知
            put("300002", true);

            //空号
            put("300003", false);

            //黑名单
            put("300004", true);

            //超频
            put("300005", false);

            //系统忙
            put("300006", true);

        }
    };
    private static Logger logger = LoggerFactory.getLogger(UcPaasUtil.class);
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    /**
     * 时间格式化
     */
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static ObjectMapper mapper = new ObjectMapper();

    private static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    /**
     * 调用云之讯接口
     * 30秒内不能给同一号码发送相同模板消息; 30秒内不能连续发同样的内容;验证码短信参数长度不能超过10位
     * 同一天同一用户不能发超过N条验证码 , 同一天同一用户不能发超过3条相同的短信
     * sendMessage 调用的post中httpClient.execute为同步
     *
     * @param accountSid 开发者账号Id
     * @param authToken  账户授权令牌
     * @param version    版本号
     * @param appId      应用Id
     * @param templateId 模板Id
     * @param to         手机号
     * @param code       验证码
     * @param minute     短信显示参数，时间
     * @return 第三方接口发送状态，成功或者失败, 失败对应具体错误码
     */
    public static Map<String, Object> sendMessage(String accountSid, String authToken, String version, String appId, String templateId, String to, String code, int minute) {
        String param = code + "," + minute;
        String timestamp = timeFormat.format(System.currentTimeMillis());
        String url = getUrl(accountSid, authToken, version, timestamp);
        Map<String, String> message = new HashMap<>();
        message.put("appId", appId);
        message.put("templateId", templateId);
        message.put("to", to);
        message.put("param", param);
        String body;
        try {
            body = mapper.writeValueAsString(message);
            body = "{\"templateSMS\":" + body + "}";
            return post(accountSid, url, timestamp, body);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException: {}", e);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("result", ConstValue.SEND_FAIL);
        return result;
    }

    /**
     * 构建请求云之讯短信服务的url
     *
     * @param accountSid 开发者账号Id
     * @param authToken  账户授权令牌
     * @param version    版本号
     * @param timestamp  签名时间戳，格式为yyyyMMddHHmmss；签名时间戳与post请求body体的时间戳相同
     * @return 请求url
     */
    private static String getUrl(String accountSid, String authToken, String version, String timestamp) {
        String signature = DigestUtils.md5Hex(accountSid + authToken + timestamp).toUpperCase();
        StringBuffer sb = new StringBuffer("https://");
        return sb.append("api.ucpaas.com")
                .append("/")
                .append(version)
                .append("/Accounts/")
                .append(accountSid)
                .append("/Messages/templateSMS")
                .append("?sig=")
                .append(signature)
                .toString();
    }

    /**
     * 云之讯短信验证码，post请求
     *
     * @param accountSid 开发者账号Id
     * @param url        请求url
     * @param timestamp  时间戳，与签名时间戳相同
     * @param body       请求体
     * @return 返回请求结果, 成功或者失败, 失败对应具体错误码
     */
    private static Map<String, Object> post(String accountSid, String url, String timestamp, String body) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", ConstValue.SEND_FAIL);
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");

        // Authorization 需base64加密
        String src = accountSid + ":" + timestamp;
        BASE64Encoder encoder = new BASE64Encoder();
        String auth = encoder.encode(src.getBytes());
        httppost.setHeader("Authorization", auth);
        BasicHttpEntity requestBody = new BasicHttpEntity();
        try {
            requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
            requestBody.setContentLength(body.getBytes("UTF-8").length);
            httppost.setEntity(requestBody);
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String requestResult = EntityUtils.toString(entity, "UTF-8");
                JSONObject resJson = JSONObject.parseObject(requestResult);
                String respCode = resJson.getJSONObject("resp").getString("respCode");
                if ("000000".equals(respCode)) {
                    EntityUtils.consume(entity);
                    result.put("response", ConstValue.MESSAGE_OK);
                } else {
                    result.put("response", respCode);
                }
                result.put("result", ConstValue.SEND_SUCCESS);
            }
        } catch (Exception e) {
            logger.error("sendMessage post error : {}", e);
        }
        return result;
    }

}
