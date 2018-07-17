package com.monkeybean.labo.predefine;

/**
 * 返回状态码
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public enum ReturnCode {
    SUCCESS(0, "Success", "请求成功"),

    /**
     * 系统及服务错误
     */
    SERVER_EXCEPTION(10001, "System, server run exception", "服务处理异常, 请稍后重试"),
    MESSAGE_APPLY_FAILED(10005, "System, apply message code error", "短信验证码申请失败"),
    DB_DATA_ERROR(10010, "System, database exception", "数据库数据异常"),

    /**
     * 业务逻辑错误
     */
    LOGIN_ERROR(20001, "account not exist or password incorrect", "帐号不存在或密码错误"),
    PWD_SIMPLE(20002, "password is too simple", "密码过于简单"),
    NEW_OLD_PWD_SAME(20003, "new pwd can't be same as the old", "新旧密码不可相同"),
    ERROR_PASSWORD(20004, "password is wrong", "密码错误"),
    ACCOUNT_ILLEGAL(20005, "account not exist in db or account has been forbidden", "账号不存在或账户被封号"),
    IDENTITY_VERIFY_FAILED(20008, "google think you are a robot", "身份认证失败，请重试"),
    MESSAGE_APPLY_TIME_LIMIT(20010, "apply message code, the interval of time is too short", "距离上次申请短信验证码的时间间隔过短"),
    MESSAGE_APPLY_NUM_LIMIT(20011, "apply message code, the frequency is up to max", "今日短信申请次数已达上限"),
    NO_UNUSED_MESSAGE_CODE(20012, "not apply message code or message code has been used", "未申请验证码或验证码已被使用，请重新申请"),
    IMAGE_CODE_INCORRECT(20015, "image code is wrong", "图片验证码错误"),
    MESSAGE_CODE_INCORRECT(20016, "message code is wrong or invalid", "短信验证码错误或失效"),
    PHONE_HAS_USED(20020, "the phone number has been used", "该手机号已注册"),
    ILLEGAL_CHARACTER_FORBID(20030, "string can't include illegal character, especially emoji", "不可包含表情符号等非法字符"),
    UPLOAD_FILE_IS_NULL(20040, "upload file is null file", "上传的文件为空"),
    FILE_PATTERN_ILLEGAL(20041, "file pattern is illegal", "上传的文件格式不合法"),
    FILE_TOO_LARGE(20042, "file size is too large", "文件尺寸太大"),
    FILE_NOT_EXIST(20043, "file doesn't exist", "文件不存在"),
    FILE_HAS_EXIST(20044, "file is exist", "文件已存在"),
    FILE_NOT_MINE(20045, "file isn't mine", "文件不属于当前用户, 无权限操作"),
    MAIL_BIND_BEFORE(20050, "mail has bind before", "账户已绑定邮箱"),
    MAIL_HAS_USED(20051, "mail has used by others", "邮箱已被其他账户绑定"),
    MAIL_KEY_HAS_EXPIRED(20052, "mail key is expired", "邮箱激活地址已失效，请重新申请"),
    MAIL_SEND_MAX(20053, "the number of mail send is up to top today", "今日邮件发送次数已达上限");

    int code;
    String msg;
    String description;

    ReturnCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
        CacheData.codeName.put(code, this.name());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
