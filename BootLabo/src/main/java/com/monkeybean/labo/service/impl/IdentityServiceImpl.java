package com.monkeybean.labo.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.monkeybean.labo.component.config.MessageConfig;
import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.res.AccountInfoRes;
import com.monkeybean.labo.predefine.CacheData;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.service.IdentityService;
import com.monkeybean.labo.service.PublicService;
import com.monkeybean.labo.service.database.LaboDoService;
import com.monkeybean.labo.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 身份相关服务
 * <p>
 * Created by MonkeyBean on 2019/1/4.
 */
@Service
public class IdentityServiceImpl implements IdentityService {
    private static Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);

    private final MessageConfig messageConfig;

    private final OtherConfig otherConfig;

    private final LaboDoService laboDoService;

    private final JavaMailSender mailSender;

    private final PublicService publicService;

    @Value("${spring.mail.username}")
    private String mailSendFrom;

    @Autowired
    public IdentityServiceImpl(MessageConfig messageConfig, OtherConfig otherConfig, LaboDoService laboDoService, JavaMailSender mailSender, PublicService publicService) {
        this.messageConfig = messageConfig;
        this.otherConfig = otherConfig;
        this.laboDoService = laboDoService;
        this.mailSender = mailSender;
        this.publicService = publicService;
    }

    /**
     * 申请短信验证码
     *
     * @param phone 手机号
     */
    public Result<String> getValidCode(String phone) {

        //申请次数是否已达上限
        if (this.laboDoService.queryTodayMessageApplyCount(phone) >= this.messageConfig.getMaxNum()) {
            return new Result<>(ReturnCode.MESSAGE_APPLY_NUM_LIMIT);
        }

        //申请时间是否合法
        Map<String, Object> messageRecord = this.laboDoService.queryLatestMessageRecord(phone, null);
        if (messageRecord != null) {
            Timestamp lastSendTime = (Timestamp) messageRecord.get("create_time");
            long applyInterval = Math.abs(lastSendTime.getTime() - System.currentTimeMillis());
            if (applyInterval < TimeUnit.SECONDS.toMillis(this.messageConfig.getIntervalTime())) {
                IdentityServiceImpl.logger.info("getValidCode, time is too short, phone: {}, applyInterval: {}", phone, applyInterval);
                return new Result<>(ReturnCode.MESSAGE_APPLY_TIME_LIMIT);
            }
        }
        String verifyCode = VerifyCodeUtil.getInstance().generateVerifyCode(this.messageConfig.getCodeLength(), this.messageConfig.isHasAlpha());

        //调试
        if (this.otherConfig.isSimulate()) {
            this.laboDoService.addMessageRecord(phone, verifyCode, "OK", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            return new Result<>(ReturnCode.SUCCESS, verifyCode);
        }

        Map<String, Object> sendResult = AliYunUtil.sendMessage(this.messageConfig.getAccessKeyId(), this.messageConfig.getAccessKeySecret(),
                this.messageConfig.getSignName(), messageConfig.getTemplateId(), phone, verifyCode);
        if (ConstValue.SEND_SUCCESS.equals(sendResult.get("result").toString())) {
            SendSmsResponse sendSmsResponse = (SendSmsResponse) sendResult.get("response");
            String responseCode = sendSmsResponse.getCode();
            String bizId = sendSmsResponse.getBizId();
            IdentityServiceImpl.logger.info("短信接口返回的数据----------------");
            IdentityServiceImpl.logger.info("Code = {}", responseCode);
            IdentityServiceImpl.logger.info("BizId = {}", bizId);
            IdentityServiceImpl.logger.info("Message = {}", sendSmsResponse.getMessage());
            IdentityServiceImpl.logger.info("RequestId = {}", sendSmsResponse.getRequestId());

            //入库
            this.laboDoService.addMessageRecord(phone, verifyCode, responseCode, bizId);
            if ("OK".equals(responseCode)) {
                return new Result<>(ReturnCode.SUCCESS);
            }
        }
        return new Result<>(ReturnCode.MESSAGE_APPLY_FAILED);
    }

    /**
     * 密码比对
     * 根据sTime做多次摘要计算，与传入密码对比,判断密码是否正确
     *
     * @param md5PasswordOrigin 正确明文密码的单次md5摘要
     * @param passwordMd5       要比对的密码
     * @param timeStr           unix时间戳，字符串格式
     * @return 成功则返回true
     */
    private boolean isPasswordRight(String md5PasswordOrigin, String passwordMd5, String timeStr) {
        int loop = Integer.parseInt(timeStr.substring(timeStr.length() - 1));
        return Coder.manyMd5PasswordCompare(md5PasswordOrigin, passwordMd5, loop);
    }

    /**
     * 账号密码登录
     *
     * @param user        账号（手机号）
     * @param passwordMd5 明文密码做了n + 1次md5, 首先进行单次md5，然后根据sTime尾数再进行n次md5
     * @param stay        是否保持登录
     * @param timeStr     前端请求时间
     * @param request     http请求
     */
    public Result<Integer> userLogin(String user, String passwordMd5, String token, boolean stay, String timeStr, HttpServletRequest request) {

        //google reCaptcha 身份认证
        String ip = IpUtil.getIpAddress(request);
        if (!this.otherConfig.isSimulate()) {
            boolean verifyStatus = ReCaptchaUtil.verifyReCaptcha(this.otherConfig.getReCaptchaSecretKey(), token, ip);
            if (!verifyStatus) {
                return new Result<>(ReturnCode.IDENTITY_VERIFY_FAILED);
            }
        } else {
            if (!this.otherConfig.getLoginTempKey().equals(token)) {
                return new Result<>(ReturnCode.IDENTITY_VERIFY_FAILED);
            }
        }

        //校验账户
        Map<String, Object> accountInfo;
        if (LegalUtil.isChinaPhoneLegal(user)) {
            accountInfo = this.laboDoService.queryAccountInfoByPhone(user);
        } else {
            accountInfo = this.laboDoService.queryAccountInfoByEmail(user);
        }
        if (accountInfo == null) {
            IdentityServiceImpl.logger.info("phoneLogin, account not exist, user: {}", user);
            return new Result<>(ReturnCode.LOGIN_ERROR);
        }

        //校验密码
        String md5PwdOrigin = Coder.decryptPsWithSlat(accountInfo.get("password").toString(), this.otherConfig.getSqlSalt());
        if (!this.otherConfig.isSimulate() && !this.isPasswordRight(md5PwdOrigin, passwordMd5, timeStr)) {
            IdentityServiceImpl.logger.info("phoneLogin, password is wrong, user: {}", user);
            return new Result<>(ReturnCode.LOGIN_ERROR);
        }
        if (!this.publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("phoneLogin, account is illegal, user: {}", user);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //账户登录信息更新、身份信息写入session
        Integer accountId = Integer.valueOf(accountInfo.get("account_id").toString());
        this.laboDoService.updateLoginInfoById(ip, accountId);
        request.getSession().setAttribute(ConstValue.ACCOUNT_IDENTITY, accountId);
        if (stay) {
            request.getSession().setMaxInactiveInterval(ConstValue.MAX_SESSION_TIME);
        }
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 用户注册
     *
     * @param phone    手机号
     * @param code     短信验证码
     * @param name     用户名
     * @param password 密码，明文的单次Md5摘要
     * @param request  http请求
     */
    public Result<Integer> userRegister(String phone, String code, String name, String password, HttpServletRequest request) {

        //非法字符检测
        if (FilterUtf8Mb4Util.containsOutOfUtf8(name)) {
            return new Result<>(ReturnCode.ILLEGAL_CHARACTER_FORBID);
        }

        //校验手机号是否已被注册
        if (this.laboDoService.queryAccountInfoByPhone(phone) != null) {
            return new Result<>(ReturnCode.PHONE_HAS_USED);
        }

        //验证码匹配
        ReturnCode verifyResult = this.verifyMessageCode(phone, code);
        if (verifyResult != ReturnCode.SUCCESS) {
            return new Result<>(verifyResult);
        }

        //生成新账户Id
        // int newAccountId = this.generateNewId();
        int newAccountId = publicService.getNewAccountId();
        String dbPwd = Coder.encryptPassWithSlat(password, otherConfig.getSqlSalt());
        String ip = IpUtil.getIpAddress(request);

        //添加新账户
        this.laboDoService.addAccountInfo(newAccountId, phone, dbPwd, name, ip);

        //身份信息写入session
        request.getSession().setAttribute(ConstValue.ACCOUNT_IDENTITY, newAccountId);
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 生成新账户Id
     */
    private int generateNewId() {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        Integer maxAccountId = this.laboDoService.queryMaxAccountId();
        String idPrefix = String.valueOf(maxAccountId / 100 + 1);
        String idSuffix = decimalFormat.format(Math.floor(Math.random() * 100));
        return Integer.parseInt(idPrefix + idSuffix);
    }

    /**
     * 短信验证码合法性判断
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 验证成功返回SUCCESS
     */
    private ReturnCode verifyMessageCode(String phone, String code) {
        Map<String, Object> messageRecord = this.laboDoService.queryLatestMessageRecord(phone, false);
        if (messageRecord != null) {
            long lastSendTime = ((Timestamp) messageRecord.get("create_time")).getTime();
            long curlTime = System.currentTimeMillis();
            long intervalTime = Math.abs(curlTime - lastSendTime);
            boolean isTimeLegal = intervalTime < TimeUnit.MINUTES.toMillis(this.messageConfig.getValidCodeTimeOut());
            if (!(isTimeLegal && code.equals(messageRecord.get("message_code").toString()))) {
                return ReturnCode.MESSAGE_CODE_INCORRECT;
            }
        } else {
            return ReturnCode.NO_UNUSED_MESSAGE_CODE;
        }

        //更新验证码使用状态
        this.laboDoService.updateMessageStatus(phone);
        return ReturnCode.SUCCESS;
    }

    /**
     * 获取账户信息
     *
     * @param accountId 账户Id
     */
    public Result<AccountInfoRes> getAccountInfo(int accountId) {
        Map<String, Object> accountInfo = this.laboDoService.queryAccountInfoById(accountId);
        if (!this.publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("getAccountInfo, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        AccountInfoRes accountInfoRes = new AccountInfoRes();
        accountInfoRes.setPhone(accountInfo.get("phone").toString());
        accountInfoRes.setNickname(accountInfo.get("nickname").toString());
        accountInfoRes.setEmail((String) accountInfo.get("email"));
        accountInfoRes.setAdmin(Boolean.parseBoolean(accountInfo.get("is_admin").toString()));
        if (accountInfo.get("avatar") != null) {
            accountInfoRes.setAvatar(Base64.encodeBase64String((byte[]) accountInfo.get("avatar")));
        }
        return new Result<>(ReturnCode.SUCCESS, accountInfoRes);
    }

    /**
     * 上传头像到临时表
     */
    public Result<String> avatarUpload(int accountId, MultipartFile file) {
        Map<String, Object> accountInfo = this.laboDoService.queryAccountInfoById(accountId);
        if (!publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("avatarUpload, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //上传的文件为空
        if (file == null) {
            IdentityServiceImpl.logger.warn("assetUpload, file is null");
            return new Result<>(ReturnCode.UPLOAD_FILE_IS_NULL);
        }
        String name = file.getOriginalFilename();
        String[] names = name.split("\\.");

        //文件名不合法
        if (names.length <= 0 || name.length() < ConstValue.IMAGE_NAME_MIN_LEN) {
            IdentityServiceImpl.logger.warn("assetUpload, file name is illegal");
            return new Result<>(ReturnCode.FILE_PATTERN_ILLEGAL);
        }
        String tail = names[names.length - 1];

        //格式不合法
        if (!ConstValue.IMAGE_SUPPORT_PATTERN.contains(tail)) {
            IdentityServiceImpl.logger.warn("assetUpload, file pattern is illegal");
            return new Result<>(ReturnCode.FILE_PATTERN_ILLEGAL);
        }
        Long time = System.currentTimeMillis();
        String fileName = "cover" + time + "." + tail;
        byte[] coverByte;
        try {
            coverByte = file.getBytes();
        } catch (IOException e) {
            IdentityServiceImpl.logger.error("IOException e: {}", e);
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
        this.laboDoService.addNewTempAsset(accountId, fileName, coverByte);
        this.laboDoService.updateAvatar(accountId, coverByte);
        return new Result<>(ReturnCode.SUCCESS, Base64.encodeBase64String(coverByte));
    }

    /**
     * 修改密码
     *
     * @param accountId       账户Id
     * @param oldPwdMd5       老密码做了n + 1次md5, 首先进行单次md5，然后根据sTime尾数再进行n次md5
     * @param newPwdSingleMd5 新密码单次md5摘要
     * @param timeStr         unix时间戳，字符串格式
     */
    public Result<Integer> updatePassword(int accountId, String oldPwdMd5, String newPwdSingleMd5, String timeStr) {
        Map<String, Object> accountInfo = this.laboDoService.queryAccountInfoById(accountId);
        if (!this.publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("updatePassword, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        String md5PwdOrigin = Coder.decryptPsWithSlat(accountInfo.get("password").toString(), this.otherConfig.getSqlSalt());
        if (md5PwdOrigin.equals(newPwdSingleMd5)) {
            IdentityServiceImpl.logger.info("updatePassword, new pwd can't be same as the old, accountId: {}", accountId);
            return new Result<>(ReturnCode.NEW_OLD_PWD_SAME);
        }

        //判断密码是否正确
        if (!this.isPasswordRight(md5PwdOrigin, oldPwdMd5, timeStr)) {
            IdentityServiceImpl.logger.info("updatePassword, password is wrong, accountId: {}", accountId);
            return new Result<>(ReturnCode.ERROR_PASSWORD);
        }
        this.laboDoService.updatePasswordById(accountId, Coder.encryptPassWithSlat(newPwdSingleMd5, this.otherConfig.getSqlSalt()));
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 重置密码
     *
     * @param phone           账户手机号
     * @param code            短信验证码
     * @param newPwdSingleMd5 明文密码单次Md5摘要
     */
    public Result<Integer> resetPassword(String phone, String code, String newPwdSingleMd5) {
        Map<String, Object> accountInfo = this.laboDoService.queryAccountInfoByPhone(phone);
        if (!this.publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("resetPassword, account is illegal, phone: {}", phone);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }
        ReturnCode verifyResult = this.verifyMessageCode(phone, code);
        if (verifyResult != ReturnCode.SUCCESS) {
            return new Result<>(verifyResult);
        }
        this.laboDoService.updatePasswordById(Integer.valueOf(accountInfo.get("account_id").toString()), Coder.encryptPassWithSlat(newPwdSingleMd5, this.otherConfig.getSqlSalt()));
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 绑定邮箱
     *
     * @param accountId 账户Id
     * @param mail      邮箱地址
     */
    public Result<String> bindMail(int accountId, String mail) {
        Map<String, Object> accountInfo = this.laboDoService.queryAccountInfoById(accountId);
        if (!this.publicService.checkAccountLegal(accountInfo)) {
            IdentityServiceImpl.logger.warn("bindMail, account is illegal, accountId: {}", accountId);
            return new Result<>(ReturnCode.ACCOUNT_ILLEGAL);
        }

        //邮箱的发送次数是否已达最大
        if (CacheData.getMailSendNumMap().getOrDefault(mail, 0) > this.otherConfig.getMailSendMaxNum()) {
            IdentityServiceImpl.logger.warn("mail send count is up to max, accountId: {}, mail: {}", accountId, mail);
            return new Result<>(ReturnCode.MAIL_SEND_MAX);
        }

        //新邮箱不可与旧邮箱相同
        if (accountInfo.get("email") != null && mail.equals(accountInfo.get("email").toString())) {
            IdentityServiceImpl.logger.info("new mail is same as the old, accountId: {}, mail: {}", accountId, mail);
            return new Result<>(ReturnCode.MAIL_SHOULD_DIFF);
        }

        //邮箱是否被其他账号使用
        if (this.laboDoService.queryAccountInfoByEmail(mail) != null) {
            IdentityServiceImpl.logger.info("mail: {} has been used by others", mail);
            return new Result<>(ReturnCode.MAIL_HAS_USED);
        }

        //传参并重定向页面
        String mailKey = this.generateMailKey();
        String activeUrl = this.otherConfig.getMailActiveAddress() + "?" + "verify=" + mailKey;
        if (!sendMail(mail, activeUrl)) {
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
        CacheData.getMailKeyMap().put(mailKey, accountInfo.get("phone").toString() + mail);
        CacheData.getMailSendNumMap().put(mail, CacheData.getMailSendNumMap().getOrDefault(mail, 0) + 1);
        return new Result<>(ReturnCode.SUCCESS);
    }

    /**
     * 生成邮箱激活key
     */
    private String generateMailKey() {
        String uuidStr = UUID.randomUUID().toString();
        return uuidStr.substring(0, 8) + DigestUtils.sha1Hex(uuidStr.substring(8));
    }

    /**
     * 发送邮件
     *
     * @param mailTo    收件方
     * @param activeUrl 邮箱激活地址
     */
    public boolean sendMail(String mailTo, String activeUrl) {
        final String subject = "账户邮箱激活";
        String content = "<html><body><h3>欢迎使用MonkeyBean&Sherry小系统, 邮箱绑定，请点击：<a href=" + activeUrl + ">激活地址</a></h3></body></html>";
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(this.mailSendFrom);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(content, true);
            this.mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            IdentityServiceImpl.logger.error("sendMail MessagingException: {}", e);
        }
        return false;
    }

}
