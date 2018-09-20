package com.monkeybean.labo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.dao.LaboDataDao;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.service.IdentityService;
import com.monkeybean.labo.util.IpUtil;
import com.monkeybean.labo.util.ReCaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by MonkeyBean on 2018/5/26.
 */
@Api(value = "测试相关api")
@RequestMapping(path = "testtest")
@RestController
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    private final OtherConfig otherConfig;

    private final IdentityService identityService;

    private final LaboDataDao laboDataDao;

    @Autowired
    public TestController(OtherConfig otherConfig, IdentityService identityService, LaboDataDao laboDataDao) {
        this.otherConfig = otherConfig;
        this.identityService = identityService;
        this.laboDataDao = laboDataDao;
    }

    @ApiOperation(value = "测试reCaptcha")
    @RequestMapping(path = "reCaptcha/verify", method = RequestMethod.POST)
    public Result<Boolean> testVerifyReCaptcha(@RequestParam(value = "response") String response, HttpServletRequest request) {
        String ip = IpUtil.getIpAddress(request);
        boolean verifyStatus = ReCaptchaUtil.verifyReCaptcha(otherConfig.getReCaptchaSecretKey(), response, ip);
        return new Result<>(ReturnCode.SUCCESS, verifyStatus);
    }

    @ApiOperation(value = "测试上传头像")
    @PostMapping(path = "avatar/upload")
    public Result<String> testAvatarUpload(@RequestParam(value = "id") int accountId, @RequestParam(value = "cover") MultipartFile file) {
        return identityService.avatarUpload(accountId, file);
    }

    @ApiOperation(value = "测试图片保存到本地目录")
    @PostMapping(path = "image/upload/local")
    public Result<String> testImageUploadLocal(@RequestParam(value = "fileCode") String fileCode, @RequestParam(value = "fileName") String fileName) throws IOException {
        String baseStorePath = otherConfig.getBaseStorePath();

        //校验文件父路径
        File rootPath = new File(baseStorePath);
        if (!rootPath.exists()) {
            logger.warn("new path is created: {}, status: {}", rootPath, rootPath.mkdirs());
        }

        //校验文件
        UUID uuid = UUID.randomUUID();
        String fileNewName = uuid + fileName.substring(fileName.lastIndexOf("."));
        String fileStorePath = rootPath.getPath() + "\\" + fileNewName;
        File imageFile = new File(fileStorePath);

        //解码
        byte[] fileBytes = Base64.decodeBase64(fileCode.getBytes());
        logger.info("image file size: {} kb", String.format("%.2f", fileBytes.length / 1024.));

        //流写入
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        InputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        IOUtils.copy(fileInputStream, outputStream);
        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(outputStream);

        String fileAccessPath = otherConfig.getBaseAccessPath() + "/" + fileNewName;
        logger.info("fileStorePath is : {}, fileAccessPath: {}", fileStorePath, fileAccessPath);
        return new Result<>(ReturnCode.SUCCESS, fileAccessPath);
    }

    /**
     * 测试sql, 参数为数组
     */
    @ApiOperation(value = "测试sql, sql参数为数组")
    @GetMapping(path = "sql/array/info")
    public Result<List<HashMap<String, Object>>> testSqlArray(@RequestParam(value = "phone") String phoneStr) {
        String[] phoneArray = phoneStr.split(",");
        List<String> phoneList = new ArrayList<>();
        for (String phone : phoneArray) {
            phoneList.add(phone);
        }
        List<HashMap<String, Object>> resultList = null;
        if (!phoneList.isEmpty()) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("table", "account");
            param.put("array", phoneList);
            resultList = laboDataDao.queryListByArray(param);
        }
        return new Result<>(ReturnCode.SUCCESS, resultList);
    }

    /**
     * 测试解析excel
     */
    @ApiOperation(value = "测试获取excel单元格数据集合")
    @PostMapping(path = "data/excel/get")
    public Result<List<Long>> testGetExcelData(@RequestParam(value = "excel") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();

        //excel 97-07
        Workbook workbook = new HSSFWorkbook(inputStream);

        //excel 07+
//        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<Long> data = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC) {
                data.add((long) cell.getNumericCellValue());
            }
        }
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    /**
     * 测试特殊请求的参数处理，get参数放到json中，post有效参数的key在get中申明
     */
    @ApiOperation(value = "特殊参数请求处理, 失败返回null")
    @PostMapping(path = "special")
    @SuppressWarnings("unchecked")
    public String testParam(HttpServletRequest request) throws UnsupportedEncodingException {

        //get json参数放到map中
        String queryString = request.getQueryString();
        JSONObject json = JSONObject.parseObject(URLDecoder.decode(queryString, "UTF-8"));
        Map<String, Object> map = JSONObject.toJavaObject(json, Map.class);
        if (!map.containsKey("postField")) {
            return null;
        }
        String[] postFields = map.get("postField").toString().split(",");
        List<String> postFieldList = Arrays.asList(postFields);

        //post参数校验并放到map中
        Enumeration<String> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String paramName = requestParamNames.nextElement();
            if (postFieldList.contains(paramName)) {
                map.put(paramName, request.getParameter(paramName));
            }
        }
        map.remove("postField");
        return JSON.toJSONString(map);
    }
    
    /**
     * 存储已登录账户的session, 通过以下三个接口验证强制单终端在线机制：cookie/diff、session/login、session/info
     * key为账户Id, value为账户最后一次登录的session
     */
    private ConcurrentHashMap<Integer, HttpSession> tempCache = new ConcurrentHashMap<>();

    /**
     * 不同浏览器的cookie不共享，因为每个浏览器存储的cookie路径不一样
     * 通过cookie中的jsessionid验证
     */
    @GetMapping(path = "cookie/diff")
    public String testDiffCookie(HttpSession session){
        return session.getId();
    }

    /**
     * 强制下线(登陆有效性局限于单个终端/浏览器)
     */
    @GetMapping(path = "session/login")
    public String testSessionLogin(HttpSession session){
        int accountId = 1;
        HttpSession oldSession = tempCache.get(accountId);
        if(oldSession != null){
            if(oldSession.getAttribute("accountId") != null){
                oldSession.invalidate();
            }
        }
        session.setAttribute("accountId", accountId);
        tempCache.put(accountId, session);
        return session.getId();
    }

    /**
     * 模拟登陆后的接口
     */
    @GetMapping(path = "session/info")
    public String getSessionInfo(HttpSession session){
        if(session.getAttribute("accountId") != null){
            return session.getId();
        }else{
            return "no info in session";
        }
    }

}
