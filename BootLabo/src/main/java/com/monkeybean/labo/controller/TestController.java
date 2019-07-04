package com.monkeybean.labo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.dao.LaboDataDao;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.predefine.ReturnCode;
import com.monkeybean.labo.service.IdentityService;
import com.monkeybean.labo.service.database.LaboDoService;
import com.monkeybean.labo.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

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

    private final LaboDoService laboDoService;

    /**
     * 存储已登录账户的session, 通过以下三个接口验证强制单终端在线机制：cookie/diff、session/login、session/info
     * key为账户Id, value为账户最后一次登录的session
     */
    private ConcurrentHashMap<Integer, HttpSession> tempCache = new ConcurrentHashMap<>();

    @Autowired
    public TestController(OtherConfig otherConfig, IdentityService identityService, LaboDataDao laboDataDao, LaboDoService laboDoService) {
        this.otherConfig = otherConfig;
        this.identityService = identityService;
        this.laboDataDao = laboDataDao;
        this.laboDoService = laboDoService;
    }

    @ApiOperation(value = "测试reCaptcha")
    @PostMapping(path = "reCaptcha/verify")
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
        String fileNewName = uuid + fileName.substring(fileName.lastIndexOf('.'));
        String fileStorePath = rootPath.getPath() + "\\" + fileNewName;
        File imageFile = new File(fileStorePath);

        //解码
        byte[] fileBytes = Base64.decodeBase64(fileCode.getBytes());
        String formatSize = String.format("%.2f", fileBytes.length / 1024.);
        logger.info("image file size: {} kb", formatSize);

        //流写入
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        InputStream fileInputStream = new ByteArrayInputStream(fileBytes);
        org.apache.tomcat.util.http.fileupload.IOUtils.copy(fileInputStream, outputStream);
        org.apache.tomcat.util.http.fileupload.IOUtils.closeQuietly(fileInputStream);
        org.apache.tomcat.util.http.fileupload.IOUtils.closeQuietly(outputStream);

        String fileAccessPath = otherConfig.getBaseAccessPath() + "/" + fileNewName;
        logger.info("fileStorePath is : {}, fileAccessPath: {}", fileStorePath, fileAccessPath);
        return new Result<>(ReturnCode.SUCCESS, fileAccessPath);
    }

    @ApiOperation(value = "测试图片简单加密后，按既定路径存储")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "origin", value = "原始根路径", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "aim", value = "目标根路径，注：不可与原始根路径存在包含关系，否则多次执行会读取已生成的文件", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping(path = "/image/rearrange")
    public Result<String> rearrangeImage(@RequestParam("origin") String originPath, @RequestParam("aim") String aimPath) {
        File originDir = new File(originPath);
        File aimDir = new File(aimPath);
        if (!originDir.exists() || !originDir.isDirectory() || !aimDir.exists() || !aimDir.isDirectory()) {
            return new Result<>(ReturnCode.OTHER_EXCEPTION);
        }
        Pattern pattern = Pattern.compile(ConstValue.LEGAL_IMAGE_NAME);
        List<File> originFileList = new ArrayList<>();
        OutputStream writer = null;
        try {
            addImgList(originDir, originFileList, pattern);
            for (File file : originFileList) {
                byte[] fileBytes = FileUtil.readAsByteArray(file);

                //仅测试，不通过md5校验文件是否已存在，直接写入
                String hash = Coder.getMd5(fileBytes);
                String mappingPath = FileCommonUtil.generateMappingPath(aimPath, hash, FilenameUtils.getExtension(file.getName()));
                logger.info("mappingPath is: {}", mappingPath);
                writer = new FileOutputStream(mappingPath);

                //仅测试，异或数字设为1224
                int xor = 1204;
                writer.write(FileCommonUtil.encryptImgXor(fileBytes, xor));
            }
            return new Result<>(ReturnCode.SUCCESS);
        } catch (Exception e) {
            logger.error("rearrangeImage, e: {}", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("rearrangeImage, write close exception, {}", e);
                }
            }
        }
        return new Result<>(ReturnCode.OTHER_EXCEPTION);
    }

    /**
     * 将某个目录下的所有图片路径添加至list
     *
     * @param directory 根目录
     * @param imgList   图片列表
     * @param pattern   图片名的正则校验
     * @throws IOException 读文件异常
     */
    private void addImgList(File directory, List<File> imgList, Pattern pattern) throws IOException {
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File tempFile : files) {
                if (tempFile.isDirectory()) {
                    addImgList(tempFile, imgList, pattern);
                } else {
                    if (!pattern.matcher(tempFile.getName()).matches()) {
                        logger.warn("addImgList, file name illegal, filePath: {}", tempFile.getAbsolutePath());
                        continue;
                    }

                    //字节大小限制设为4M
                    double maxSize = 4096.0;
                    if (!FileCommonUtil.isFileSizeLegal(FileUtil.readAsByteArray(tempFile).length, maxSize)) {
                        logger.warn("addImgList, file too large, filePath: {}", tempFile.getAbsolutePath());
                        continue;
                    }
                    imgList.add(tempFile);
                }
            }
        }
    }

    /**
     * 测试sql, 参数为数组
     */
    @ApiOperation(value = "测试sql, sql参数为数组")
    @GetMapping(path = "sql/array/info")
    public Result<List<Map<String, Object>>> testSqlArray(@RequestParam(value = "phone") String phoneStr) {
        String[] phoneArray = phoneStr.split(",");
        List<String> phoneList = new ArrayList<>();
        Collections.addAll(phoneList, phoneArray);
        List<Map<String, Object>> resultList = null;
        if (!phoneList.isEmpty()) {
            Map<String, Object> param = new HashMap<>();
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
        // Workbook workbook = new XSSFWorkbook(inputStream);
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

    @ApiOperation(value = "测试导出excel")
    @GetMapping(path = "excel/data/achieve", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void testAchieveExcel(HttpServletResponse response) {

        //测试数据
        int dataNum = 3;
        String[] columnNames = {"id", "data"};
        List<Map<String, Object>> originData = new ArrayList<>();
        for (int i = 0; i < dataNum; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put(columnNames[0], "test" + i);
            data.put(columnNames[1], Math.random());
            originData.add(data);
        }

        //application/octet-stream, 设置为二进制流的下载类型, 可实现任意格式的文件下载; ISO8859-1即Latin1, 单字节编码, 编码范围为0x00-0xFF, 向下兼容ASCII
        response.setContentType("application/octet-stream;charset=ISO8859-1");

        //Content-Disposition响应头, 设置文件在浏览器打开还是下载: inline为在页面内打开, attachment为弹出保存框下载
        String fileName = "test.xls";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        //缓存控制，强制页面无缓存。Pragma:no-cache可以应用到http 1.0和http 1.1; 而Cache-Control:no-cache是http 1.1提供的, 只能应用于http 1.1
        response.setHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        try {
            OutputStream outputStream = response.getOutputStream();
            try (Workbook workbook = ExcelUtil.createWorkBook(originData, "test_sheet", columnNames)) {
                workbook.write(outputStream);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("testAchieveExcel, IOException: {}", e);
        }
    }

    /**
     * 测试特殊请求的参数处理，get参数放到json中，post有效参数的key在get中申明
     */
    @ApiOperation(value = "特殊参数请求处理, 失败返回null")
    @PostMapping(path = "special")
    @SuppressWarnings("unchecked")
    public String testParam(HttpServletRequest request) throws UnsupportedEncodingException {
        final String postFieldKey = "postField";

        //get json参数放到map中
        String queryString = request.getQueryString();
        JSONObject json = JSONObject.parseObject(URLDecoder.decode(queryString, "UTF-8"));
        Map<String, Object> map = JSONObject.toJavaObject(json, Map.class);
        if (!map.containsKey(postFieldKey)) {
            return null;
        }
        String[] postFields = map.get(postFieldKey).toString().split(",");
        List<String> postFieldList = Arrays.asList(postFields);

        //post参数校验并放到map中
        Enumeration<String> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String paramName = requestParamNames.nextElement();
            if (postFieldList.contains(paramName)) {
                map.put(paramName, request.getParameter(paramName));
            }
        }
        map.remove(postFieldKey);
        return JSON.toJSONString(map);
    }

    /**
     * 不同浏览器的cookie不共享，因为每个浏览器存储的cookie路径不一样
     * 通过cookie中的jsessionid验证
     */
    @ApiOperation(value = "测试不同浏览器的cookie不共享")
    @GetMapping(path = "cookie/diff")
    public String testDiffCookie(HttpSession session) {
        return session.getId();
    }

    /**
     * 强制下线(登陆有效性局限于单个终端/浏览器)
     */
    @ApiOperation(value = "登录有效性验证")
    @GetMapping(path = "session/login")
    public String testSessionLogin(HttpSession session) {
        int accountId = 1;
        HttpSession oldSession = tempCache.get(accountId);
        if (oldSession != null && oldSession.getAttribute(ConstValue.ACCOUNT_IDENTITY) != null) {
            oldSession.invalidate();
        }
        session.setAttribute(ConstValue.ACCOUNT_IDENTITY, accountId);
        tempCache.put(accountId, session);
        return session.getId();
    }

    /**
     * 模拟登陆后的接口
     */
    @ApiOperation(value = "获取session信息")
    @GetMapping(path = "session/info")
    public String getSessionInfo(HttpSession session) {
        if (session.getAttribute(ConstValue.ACCOUNT_IDENTITY) != null) {
            return session.getId();
        } else {
            return "no info in session";
        }
    }

    @ApiOperation(value = "测试反射调用")
    @GetMapping(path = "method/invoke")
    public List<Map<String, Object>> testInvokeMethod() {
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 20);
        params.put("offset", 0);
        return laboDoService.testReflectionInvoke(params, "queryProjectInfoList");
    }

    @ApiOperation(value = "获取json配置中的第n条数据")
    @ApiImplicitParam(name = "order", value = "数据索引", required = true, dataType = "int", paramType = "query")
    @GetMapping(path = "tmp/data/json")
    public Result<String> getTmpJsonData(@RequestParam(value = "order") int order) {
        try {
            Resource resource = new ClassPathResource("info_config.json");
            String content = FileCommonUtil.getContent(resource.getFile());
            JSONArray array = JSONArray.parseArray(content);
            if (order > 0 && order <= array.size()) {
                return new Result<>(ReturnCode.SUCCESS, array.getString(order - 1));
            }
            return new Result<>(ReturnCode.SUCCESS);
        } catch (IOException e) {
            logger.error("getTmpJsonData, IOException, e: {}", e);
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
    }

    @ApiOperation(value = "获取json配置中的所有数据")
    @GetMapping(path = "tmp/json/all")
    public Result<JSONArray> getAllTmpJsonData() {
        Resource resource = new ClassPathResource("info_config.json");
        try (FileInputStream fis = new FileInputStream(resource.getFile())) {
            String jsonStr = IOUtils.toString(fis, StandardCharsets.UTF_8);
            return new Result<>(ReturnCode.SUCCESS, JSONObject.parseArray(jsonStr));
        } catch (IOException e) {
            logger.error("getAllTmpJsonData, IOException, e: {}", e);
            return new Result<>(ReturnCode.SERVER_EXCEPTION);
        }
    }

    @ApiOperation(value = "测试上传文件到小米服务器")
    @PostMapping(path = "image/mi/upload")
    public String uploadImageToMi(@RequestParam(value = "secret") String secret, @RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        String[] fileInfo = multipartFile.getOriginalFilename().split("\\.");
        File file = File.createTempFile(fileInfo[0], "." + fileInfo[1]);
        multipartFile.transferTo(file);
        return MiPushUtil.pushFile(file, secret, true);
    }

    @ApiOperation(value = "测试更改默认json解析")
    @GetMapping(path = "json/default/check")
    // public Result<Map<Integer, String>> checkJsonSeq(){
    public Result<String> checkJsonSeq() {

        //LinkedHashMap是有序的，保存了记录的插入顺序；若已有记录，后续值变更不影响原有顺序
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(4, "four");
        map.put(3, "three");
        map.put(4, "four_twice");

        //SpringBoot内置了jackson来完成JSON的序列化和反序列化, 若将map直接响应请求，则会调用jackson序列化, 而jackson序列化出的数据是无序的。
        //可通过gson或fastjson将map序列化为json字符串, 保证有序, 但是这种方法更改了返回数据类型(map变成了String)
        //换其他方法，如更改默认json转换依赖：pom排除jackson，引入fastjson或gson, 同时application.properties声明首选库
        //暂不更改默认jackson, 若更改，影响swagger等正常使用
        Gson gson = new Gson();
        String result1 = gson.toJson(map);
        //String result2 = JSON.toJSONString(map);
        //return new Result<>(ReturnCode.SUCCESS, map);
        return new Result<>(ReturnCode.SUCCESS, result1);
    }

    @ApiOperation(value = "测试图片正则")
    @GetMapping(path = "image/regex/{imageName}")
    public String testImageRegex(@PathVariable String imageName) {
        if (otherConfig.getImagePattern().matcher(imageName).matches()) {
            return "match";
        }
        return "mismatch";
    }

    @ApiOperation(value = "测试调用shell/python/bat等, 读取执行结果")
    @ApiImplicitParam(name = "file", value = "脚本路径", required = true, dataType = "string", paramType = "query")
    @GetMapping(path = "script/call")
    public String callScript(String file) {
        if (StringUtils.isNotEmpty(file)) {
            return CommonUtil.callScript(file);
        }
        logger.warn("callScript, param illegal");
        return null;
    }

    @ApiOperation(value = "测试linux下解析mobileprovision文件")
    @ApiImplicitParam(name = "file", value = "全路径的文件名", required = true, dataType = "string", paramType = "query")
    @GetMapping(path = "mobileprovision/parse")
    public String testParseMobileProvision(String file) {
        if (StringUtils.isNotEmpty(file)) {
            return IpaUtil.getFileContentByExecuteShell(file);
        }
        logger.warn("testParseMobileProvision, param illegal");
        return null;
    }

    @ApiOperation(value = "测试使用copyInputStreamToFile将MultipartFile转File")
    @ApiImplicitParam(name = "path", value = "转为file的存储路径", required = true, dataType = "string", paramType = "query")
    @PostMapping(path = "multipart/to/file")
    public String testMultipartFileToFile(MultipartFile multipartFile, String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            try {
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(path + File.separator + multipartFile.getOriginalFilename()));
                return "success";
            } catch (IOException e) {
                logger.error("testMultipartFileToFile, IOException: {}", e);
            }
        }
        return "fail";
    }

    @ApiOperation(value = "测试重设图片尺寸")
    @PostMapping(path = "image/resize")
    public void testResizeImage(int width, int height, MultipartFile mFile, HttpServletResponse response) throws IOException {
        String fileName = "AppIcon" + width + "x" + height + ".png";
        response.setContentType("image/png");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        Thumbnails.of(mFile.getInputStream()).width(width).height(height).toOutputStream(servletOutputStream);
    }

    @ApiOperation(value = "测试解析ipa文件, 生成tar.gz文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domain", value = "域名前缀, 格式如https://home02.nm.erduosmj.com/package_ios", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "包名(标识符), 格式如ReDx001", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping(path = "ipa/parse/to/tar")
    public void testParseIpaToTar(MultipartFile mFile, String domain, String name) {

        //MultipartFile transfer to File
        String ipaOriginFileName = mFile.getOriginalFilename();
        File ipaOriginFile;
        try {
            ipaOriginFile = File.createTempFile("temp_File", ipaOriginFileName);
            mFile.transferTo(ipaOriginFile);
        } catch (IOException e) {
            logger.error("createTempFile IOException: {}", e);
            return;
        }

        //解析ipa文件, 生成目标文件夹
        Map<String, Object> dataMap = IpaUtil.generateDirByIpa(ipaOriginFile, ipaOriginFileName, domain, name);
        if (dataMap == null) {
            logger.error("testParseIpaToTar, generateDirByIpa failed");
            return;
        }
        logger.info("certificateProvider: {}", dataMap.get("teamName"));

        //压缩为tar.gz
        String desPath = dataMap.get("desPath").toString();
        String compressName = dataMap.get("compressName").toString();
        File inputFile = new File(desPath);
        String compressPath = desPath + File.separator + compressName;
        FileCommonUtil.compressToTar(inputFile, compressPath);

        //删除临时文件, 释放资源占用
        boolean deleteTempFile = ipaOriginFile.delete();
        if (!deleteTempFile) {
            logger.warn("deleteTempFile failed");
        }
    }

    @ApiOperation(value = "测试ftp上传, 非线程池方式")
    @PostMapping(path = "ftp/upload")
    public boolean testUploadFileToFtp(MultipartFile mFile, String url, String user, String password, String path) throws IOException {
//        File file = File.createTempFile("temp_File", mFile.getOriginalFilename());
//        mFile.transferTo(file);
//        boolean uploadRes = FtpUtil.uploadFile(url, user, password, path, mFile.getOriginalFilename(), new FileInputStream(file));
//        file.delete();
//        return uploadRes;
        return FtpUtil.uploadFile(url, user, password, path, mFile.getOriginalFilename(), mFile.getInputStream());
    }

    @ApiOperation(value = "测试ftp上传, 线程池方式")
    @PostMapping(path = "ftp/upload/pool")
    public boolean testUploadFileToFtpByPool(MultipartFile mFile) throws IOException {
        if (mFile == null) {
            return false;
        }
        String originFilePath = mFile.getOriginalFilename();
        String desFilePath = "test/" + mFile.getOriginalFilename();
        boolean uploadResult = FtpUtil.uploadFileToFtp(mFile.getInputStream(), originFilePath, true);
        boolean moveResult = FtpUtil.moveFileToDirectory(originFilePath, desFilePath);
        boolean deleteResult = FtpUtil.deleteFile(desFilePath);
        return uploadResult && moveResult && deleteResult;
    }

}
