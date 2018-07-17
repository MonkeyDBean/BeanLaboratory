// import hashCoreCrypt from "./core.js";
// import hashMd5Crypt from "./md5.js";
// import base64Crypt from "./enc-base64.js";

/**
 * Md5摘要,转为16进制字符串(Md5字节数组转String默认即是十六进制)
 */
function md5HexUtil(value){
    return CryptoJS.MD5(value).toString(CryptoJS.enc.HEX);
}

/**
 * Md5摘要,直接将字节流转为Base64
 * 方法一：
 */
function md5ByteDirectToBase64UtilMethod1(value){
    return CryptoJS.MD5(value).toString(CryptoJS.enc.Base64);
}

/**
 * Md5摘要,直接将字节流转为Base64
 * 方法二：
 */
function md5ByteDirectToBase64UtilMethod2(value){
    var base64 = CryptoJS.enc.Base64.stringify(CryptoJS.MD5(value));
    return base64;
}

/**
 * base64编码
 */
function base64Encode(value){
    var wordArray = CryptoJS.enc.Utf8.parse(value);
    var base64 = CryptoJS.enc.Base64.stringify(wordArray);
    return base64;
}

/**
 * base64解码
 */
function base64Decode(base64){
    var parsedWordArray = CryptoJS.enc.Base64.parse(base64);
    var parsedStr = parsedWordArray.toString(CryptoJS.enc.Utf8);
    return parsedStr;
}