package com.monkeybean.labo.controller;

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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

}
