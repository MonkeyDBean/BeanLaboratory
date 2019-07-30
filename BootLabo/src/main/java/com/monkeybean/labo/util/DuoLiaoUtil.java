package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装多聊接口
 * 1.注册多聊开放平台(http://duoliao.nbmenghai.com)后进入, 在商户信息中添加服务器ip白名单, 联系多聊客服审核
 * 2.新建应用(若仅用服务端接口,参数随意填写,此应用用于群助手关联), 若为客户端使用sdk, 联系客服将应用上线(只有上线应用的appId和appSecret是有效的, 可供客户端调用), 服务端接口无需将应用上线(使用的是商户id和商户密钥)
 * 3.新建群助手(关联上一步新建的应用, 名称、头像、介绍、按钮文字用于显示, 按钮url填写目标页面), 类似钉钉群助手, 作用相当于授权
 * 4.下载多聊app, 发起群聊(人数至少三人), 群聊中添加群助手(仅群主有权限看到及操作, 每个群只可添加一个群助手), 搜索上一步的群助手名称, 添加
 * 5.群内点击群助手跳转至目标页面, url携带多聊返回的群id参数
 * <p>
 * Created by MonkeyBean on 2019/7/17.
 */
public class DuoLiaoUtil {
    /**
     * 请求多聊返回json格式, 格式为：{"resTodo":"01","resMsg":"成功","resData":[]}, resTodo返回码说明如下
     */
    public static final ConcurrentHashMap<String, String> responseCodeMap = new ConcurrentHashMap<String, String>() {
        {
            //resTodo值为0, 此值为整型0而非字符串
            put("0", "userInfo参数不合法");
            put("01", "成功");
            put("02", "公共请求参数错误");
            put("03", "签名错误");

            //需将域名或ip添加到商户管理后台白名单，并联系审核通过
            put("04", "域名或ip未备案");

            //使用的商户appid而非应用的appid
            put("05", "商户的appid不正确");
            put("07", "群相关不存在");
            put("08", "该群未添加群助手或群助手已停用");
            put("98", "图片上传格式错误");
        }
    };
    /**
     * 推送地址
     */
    private static final String recordPushUnbindUrl = "http://dlappapi.nbmenghai.com/index.php/Oauth/recordPushUnbind";
    /**
     * 版本号, 固定为1.1
     */
    private static final String version = "1.1";
    /**
     * 签名算法, 固定为SHA256
     */
    private static final String signType = "SHA256";
    private static Logger logger = LoggerFactory.getLogger(DuoLiaoUtil.class);

    /**
     * 发送战绩信息
     *
     * @param tid        多聊群Id
     * @param msgTitle   消息标题
     * @param msgContent 消息内容
     * @param userInfo   用户信息, json数组格式, 其中headstr为用户头像地址, g_name为用户昵称, score为分数, game_id为用户Id,
     *                   格式如：[{"headstr":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563431510935&di=9d2246d4caeeffd811c2f4d9e2d101a7&imgtype=0&src=http%3A%2F%2Fpic.pc6.com%2Fup%2F2013-9%2F2013923115413.png","g_name":"昵称1","score":"500","game_id":"11111"},{"headstr":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563431535243&di=962daaa6900d6c61448b83a76588f627&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F26%2F20160626222622_QkZ4j.thumb.700_0.jpeg","g_name":"昵称2","score":"1000","game_id":"22222"}]
     * @param goUrl      详情跳转url
     * @param recordId   战绩Id(房间Id)
     * @param recordTime 战绩时间(开局时间), 格式为yyyy-MM-dd HH:mm:ss
     * @param appId      平台商户id
     * @param appSecret  商户密钥
     * @return 失败返回null, 成功返回json字符串
     */
    public static String pushRecord(String tid, String msgTitle, String msgContent, String userInfo, String goUrl, String recordId, String recordTime, String appId, String appSecret) {

        //校验用户信息格式是否合法
        try {

            //官方仅php接入示例, 使用json_encode编码userInfo, 编码后为json数组, php编码测试如下(在线编译：https://c.runoob.com/compile)：
            //<?php echo json_encode([['headstr'=>'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563431510935&di=9d2246d4caeeffd811c2f4d9e2d101a7&imgtype=0&src=http%3A%2F%2Fpic.pc6.com%2Fup%2F2013-9%2F2013923115413.png','g_name'=>'昵称1','score'=>'+500','game_id'=>'11111'],['headstr'=>'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563431535243&di=962daaa6900d6c61448b83a76588f627&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F26%2F20160626222622_QkZ4j.thumb.700_0.jpeg','g_name'=>'昵称2','score'=>'+1000','game_id'=>'22222']]);?>
            JSONObject.parseArray(userInfo);
        } catch (Exception e) {
            logger.warn("userInfo is illegal, userInfo is: [{}], e: [{}]", userInfo, e);
            return null;
        }

        //校验recordTime格式是否合法
        if (!LegalUtil.isLegalTime(recordTime)) {
            logger.warn("recordTime is illegal, recordTime is: [{}]", recordTime);
            return null;
        }

        //重设goUrl
        if (StringUtils.isEmpty(goUrl)) {
            goUrl = "";
        }

        //接口参数
        JSONObject customData = new JSONObject();
        customData.put("tid", tid);
        customData.put("msgTitle", msgTitle);
        customData.put("userInfo", userInfo);
        customData.put("msgContent", msgContent);
        customData.put("goUrl", goUrl);
        customData.put("recordId", recordId);
        customData.put("recordTime", recordTime);

        //公共参数
        String apiContent = customData.toJSONString();
        Map<String, Object> commonData = new HashMap<>();
        commonData.put("appid", appId);
        commonData.put("version", version);
        commonData.put("timestamp", recordTime);
        commonData.put("signType", signType);
        commonData.put("apiContent", customData.toJSONString());

        //签名原始数据要求严格顺序, 参数为空时参数名必须存在且公共请求参数顺序不可变
        String originData = "appid=" + appId + "&version=" + version + "&timestamp=" + recordTime + "&signType=" + signType + "&apiContent=" + apiContent + "&appsecret=" + appSecret;
        String sign;
        try {
            sign = Coder.encryptHmacSha256(originData, appSecret);
        } catch (Exception e) {
            logger.error("encryptHmacSha256 error: [{}]", e);
            return null;
        }
        commonData.put("sign", sign);
        return ApacheHttpUtil.doPostForm(recordPushUnbindUrl, commonData);
    }
}
