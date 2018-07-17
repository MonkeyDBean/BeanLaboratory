package com.monkeybean.labo.util;

/**
 * 验证码生成工具
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class VerifyCodeUtil {

    private static VerifyCodeUtil verifyCodeUtil = null;

    public static VerifyCodeUtil getInstance() {
        if (verifyCodeUtil == null) {
            verifyCodeUtil = new VerifyCodeUtil();
        }
        return verifyCodeUtil;
    }

    /**
     * 生成验证码
     *
     * @param codeLength 验证码的长度
     * @param hasAlpha   是否包含字母，否的话生成的验证码只有纯数字
     * @return 验证码字符串
     */
    public String generateVerifyCode(int codeLength, boolean hasAlpha) {
        RandomStringUtil randomUtil = new RandomStringUtil(codeLength);
        return randomUtil.nextString(hasAlpha);
    }

}
