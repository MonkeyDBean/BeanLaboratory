package com.monkeybean.dingtalk.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端Api文档：https://open-doc.dingtalk.com/microapp/serverapi2/gh60vz
 * 全局错误码：https://open-doc.dingtalk.com/microapp/faquestions/rftpfg
 * <p>
 * Created by MonkeyBean on 2019/4/19.
 */
public class DingUtil {
    private static final Logger logger = LoggerFactory.getLogger(DingUtil.class);
    /**
     * 钉钉api根路径
     */
    private static final String dingUrl = "https://oapi.dingtalk.com";
    /**
     * 获取token地址
     */
    private static final String dingGetTokenUrl = dingUrl + "/gettoken";
    /**
     * 获取jsApi ticket地址
     */
    private static final String dingGetJsApiTicketUrl = dingUrl + "/get_jsapi_ticket";
    /**
     * 获取用户信息地址
     */
    private static final String dingGetUserInfoUrl = dingUrl + "/user/get";
    /**
     * 获取部门用户详情地址
     */
    private static final String dingDepartmentUserUrl = dingUrl + "/user/listbypage";
    /**
     * 获取部门列表地址
     */
    private static final String dingDepartmentList = dingUrl + "/department/list";
    /**
     * 获取部门详情地址
     */
    private static final String dingDepartmentInfo = dingUrl + "/department/get";
    /**
     * 获取角色列表地址
     */
    private static final String dingRoleList = dingUrl + "/topapi/role/list";
    /**
     * 获取角色下的员工列表地址
     */
    private static final String dingRoleUserInfo = dingUrl + "/topapi/role/simplelist";
    /**
     * 成功返回码
     */
    private static int successCode = 0;
    /**
     * 授权token
     */
    private static String accessToken = null;
    /**
     * token授权时间
     */
    private static Long lastTokenTime = null;
    /**
     * ticket授权时间
     */
    private static Long lastTicketTime = null;
    /**
     * 授权ticket
     */
    private static String ticket = null;
    /**
     * 官方约定token有效时间为两小时, 这里设置为115分钟
     */
    private static long tokenAuthenticationTime = 1000 * 60 * 115L;
    /**
     * 官方约定ticket有效时间为两小时, 这里设置为115分钟
     */
    private static long ticketAuthenticationTime = 1000 * 60 * 115L;

    /**
     * 获取授权token
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回accessToken
     */
    private static String getToken(String appKey, String appSecret) {
        if (StringUtils.isAnyEmpty(appKey, appSecret)) {
            return null;
        }
        long curTime = System.currentTimeMillis();
        if (lastTokenTime == null || accessToken == null || Math.abs(curTime - lastTokenTime) > tokenAuthenticationTime) {
            accessToken = getAccessToken(appKey, appSecret);
        }
        return accessToken;
    }

    /**
     * 请求获取授权token
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回accessToken
     */
    private static String getAccessToken(String appKey, String appSecret) {
        DefaultDingTalkClient client = new DefaultDingTalkClient(dingGetTokenUrl);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            logger.error("getAccessToken ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("gettokenResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response.getAccessToken();
    }

    /**
     * 获取前端授权使用的ticket
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回ticket
     */
    public static String getTicket(String appKey, String appSecret) {
        if (StringUtils.isAnyEmpty(appKey, appSecret)) {
            return null;
        }
        long curTime = System.currentTimeMillis();
        if (lastTicketTime == null || ticket == null || Math.abs(curTime - lastTicketTime) > ticketAuthenticationTime) {
            ticket = getJsApiTicket(appKey, appSecret);
        }
        return ticket;
    }

    /**
     * 请求获取jsApi_ticket
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @return 失败返回null, 成功返回ticket
     */
    private static String getJsApiTicket(String appKey, String appSecret) {
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DefaultDingTalkClient client = new DefaultDingTalkClient(dingGetJsApiTicketUrl);
        OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
        req.setTopHttpMethod("GET");
        OapiGetJsapiTicketResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getJsApiTicket ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("getJsApiTicketResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response.getTicket();
    }

    /**
     * 获取前端校验所需签名
     *
     * @param ticket    票据
     * @param nonceStr  随机串
     * @param timeStamp 生成签名的时间戳
     * @param url       当前网页的URL，不包含#及其后面部分
     */
    public static String sign(String ticket, String nonceStr, long timeStamp, String url) {
        String originData = "jsapi_ticket=" + ticket
                + "&noncestr=" + nonceStr
                + "&timestamp=" + timeStamp
                + "&url=" + url;
        return DigestUtils.sha1Hex(originData);
    }

    /**
     * 获取某个用户信息
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @param userId    用户Id
     * @return 失败返回null
     */
    public static OapiUserGetResponse getUserInfo(String appKey, String appSecret, String userId) {
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingGetUserInfoUrl);
        OapiUserGetRequest req = new OapiUserGetRequest();
        req.setUserid(userId);
        req.setHttpMethod("GET");
        OapiUserGetResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getUserInfo ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("userGetResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 获取部门用户详情
     *
     * @param appKey       应用key
     * @param appSecret    应用密钥
     * @param departmentId 部门id，1表示根部门
     * @param size         分页大小, 最大100
     * @param offset       分页偏移
     */
    public static OapiUserListbypageResponse getDepartmentUserInfo(String appKey, String appSecret, long departmentId, long size, long offset) {
        if (departmentId < 1 || size <= 0 || size > 100 || offset < 0) {
            return null;
        }
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingDepartmentUserUrl);
        OapiUserListbypageRequest req = new OapiUserListbypageRequest();
        req.setDepartmentId(departmentId);
        req.setSize(size);
        req.setOffset(offset);

        //entry_asc(代表按照进入部门的时间升序), entry_desc(代表按照进入部门的时间降序)，modify_asc(代表按照部门信息修改时间升序)
        //modify_desc(代表按照部门信息修改时间降序), custom(代表用户定义(未定义时按照拼音)排序)
        req.setOrder("entry_desc");
        req.setHttpMethod("GET");
        OapiUserListbypageResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getDepartmentUserInfo ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("userListByPageRequest not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 获取部门列表
     *
     * @param appKey       应用key
     * @param appSecret    应用密钥
     * @param departmentId 父部门id，根部门传1
     * @param fetchChild   是否递归部门的全部子部门
     * @return 失败返回null
     */
    public static OapiDepartmentListResponse getDepartmentList(String appKey, String appSecret, String departmentId, boolean fetchChild) {
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingDepartmentList);
        OapiDepartmentListRequest req = new OapiDepartmentListRequest();
        req.setId(departmentId);
        req.setHttpMethod("GET");
        req.setFetchChild(fetchChild);
        OapiDepartmentListResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getDepartmentList ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("departmentListResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 获取部门信息
     *
     * @param appKey       应用key
     * @param appSecret    应用密钥
     * @param departmentId 部门id
     * @return 失败返回null
     */
    public static OapiDepartmentGetResponse getDepartmentInfo(String appKey, String appSecret, String departmentId) {
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingDepartmentInfo);
        OapiDepartmentGetRequest req = new OapiDepartmentGetRequest();
        req.setId(departmentId);
        req.setHttpMethod("GET");
        OapiDepartmentGetResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getDepartmentInfo ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("departmentGetResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 获取角色列表
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @param size      分页大小
     * @param offset    分页偏移
     * @return 失败返回null
     */
    public static OapiRoleListResponse getRoleList(String appKey, String appSecret, long size, long offset) {
        if (size <= 0 || size > 200 || offset < 0) {
            return null;
        }
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingRoleList);
        OapiRoleListRequest req = new OapiRoleListRequest();

        //默认值：20，最大值200
        req.setSize(size);

        //分页偏移，默认值：0
        req.setOffset(offset);
        OapiRoleListResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getRoleList ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("roleListResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 获取角色下的员工列表
     *
     * @param appKey    应用key
     * @param appSecret 应用密钥
     * @param roleId    角色Id
     * @param size      分页大小
     * @param offset    分页偏移
     * @return 失败返回null
     */
    public static OapiRoleSimplelistResponse getRoleUserList(String appKey, String appSecret, long roleId, long size, long offset) {
        if (roleId < 0 || size <= 0 || size > 200 || offset < 0) {
            return null;
        }
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient(dingRoleUserInfo);
        OapiRoleSimplelistRequest req = new OapiRoleSimplelistRequest();

        //默认值：20，最大值200
        req.setSize(size);

        //分页偏移，默认值：0
        req.setOffset(offset);
        req.setRoleId(roleId);
        OapiRoleSimplelistResponse response;
        try {
            response = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error("getRoleUserList ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("roleSimpleListResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }

    /**
     * 发送群消息, 仅测试文本消息
     *
     * @param appKey      应用key
     * @param appSecret   应用密钥
     * @param chatId      会话id
     * @param textContent 文本消息内容
     * @return 失败返回null
     */
    public static OapiChatSendResponse sendTextToChat(String appKey, String appSecret, String chatId, String textContent) {
        String accessToken = getToken(appKey, appSecret);
        if (accessToken == null) {
            return null;
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest request = new OapiChatSendRequest();
        request.setChatid(chatId);
        OapiChatSendRequest.Msg msg = new OapiChatSendRequest.Msg();
        msg.setMsgtype("text");
        OapiChatSendRequest.Text text = new OapiChatSendRequest.Text();
        text.setContent(textContent);
        msg.setText(text);
        request.setMsg(msg);
        OapiChatSendResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            logger.error("sendTextToChat ApiException, errCode: {}, errMsg: {}, subErrCode: {}, subErrMsg: {}",
                    e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getErrMsg());
            return null;
        }
        if (response.getErrcode() != null && response.getErrcode().intValue() != successCode) {
            logger.warn("chatSendResponse not success, errCode: {}", response.getErrcode());
            return null;
        }
        return response;
    }
}
