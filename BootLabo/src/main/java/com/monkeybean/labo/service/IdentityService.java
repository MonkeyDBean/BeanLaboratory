package com.monkeybean.labo.service;

import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.res.AccountInfoRes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
public interface IdentityService {

    Result<String> getValidCode(String phone);

    Result<Integer> userLogin(String user, String passwordMd5, String token, boolean stay, String timeStr, HttpServletRequest request);

    Result<Integer> userRegister(String phone, String code, String name, String password, HttpServletRequest request);

    Result<AccountInfoRes> getAccountInfo(int accountId);

    Result<String> avatarUpload(int accountId, MultipartFile file);

    Result<Integer> updatePassword(int accountId, String oldPwdMd5, String newPwdSingleMd5, String timeStr);

    Result<Integer> resetPassword(String phone, String code, String newPwdSingleMd5);

    Result<String> bindMail(int accountId, String mail);

    boolean sendMail(String mailTo, String activeUrl);

}




