package com.monkeybean.labo.controller;

import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.component.reqres.req.*;
import com.monkeybean.labo.predefine.ConstValue;
import com.monkeybean.labo.service.OperationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户操作接口
 * <p>
 * Created by MonkeyBean on 2018/5/26.
 */
@Api(value = "用户操作模块")
@RequestMapping(path = "operation")
@RestController
public class OperationController {

    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @ApiOperation(value = "上传单张图片, 返回图片url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file64", value = "base64编码的图片文件", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "文件名, 合法格式为png, jpg, gif, jpeg, 不区分大小写", required = true, dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "image/upload")
    public Result<String> imageUpload(@Valid @ModelAttribute ImageUploadReq reqModel, HttpSession session) {
        return operationService.imageUpload(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getFile64(), reqModel.getName());
    }

    @ApiOperation(value = "获取图片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sharetype", value = "图片共享类型，0为私有，1为共享, 2为账户所有图片", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页，第一页为1", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @GetMapping(value = "list/image/get")
    public Result<Map<String, Object>> getImageList(@Valid ImageListReq reqModel, HttpSession session) {
        return operationService.getImageList(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getSharetypeInt(), reqModel.getCurrentInt(), reqModel.getSizeInt());
    }

    @ApiOperation(value = "更新图片名称及图片描述")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "图片Id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "图片名称", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "des", value = "图片描述", dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "image/info/change")
    public Result<String> changeImageInfo(@Valid @ModelAttribute ImageInfoChangeReq reqModel, HttpSession session) {
        return operationService.changeImageInfo(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getIdInt(), reqModel.getName(), reqModel.getDes());
    }

    @ApiOperation(value = "图片状态更改：共享状态更改或删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "图片Id", required = true, allowMultiple = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "operate", value = "操作类型，1为共享状态更改，2为删除", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "image/status/change")
    public Result<String> changeImageStatus(@Valid @ModelAttribute ImageStatusChangeReq reqModel, HttpSession session) {
        return operationService.changeImageStatus(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getId(), reqModel.getOperateInt());
    }

    @ApiOperation(value = "多张图片上传, 前端可限制一次最多上传9张")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "image/multi/upload")
    public Result<List<String>> uploadMultiImage(@RequestParam(value = "fileImg") MultipartFile[] fileImg, HttpSession session) {
        return operationService.uploadMultiImage(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), fileImg);
    }

    @ApiOperation(value = "查询其他项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "项目类型，0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页，第一页为1", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "total", value = "查询的记录总数,首次请求即第一页时传任意非负整数，非首页传后端返回的定值", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @GetMapping(value = "info/project/other/get")
    public Result<Map<String, Object>> getOtherProjectInfo(@Valid OtherProjectInfoReq reqModel, HttpSession session) {
        return operationService.getOtherProjectInfo(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getTypeInt(),
                reqModel.getCurrentInt(), reqModel.getSizeInt(), reqModel.getTotalInt());
    }

    @ApiOperation(value = "新增项目记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "项目类型，0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "项目名称", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "访问链接", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "image", value = "缩略图url，可不传", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "des", value = "项目描述，可不传", dataType = "string", paramType = "query")
    })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "无特殊处理的返回值")})
    @PostMapping(value = "info/project/other")
    public Result<String> addOtherProjectInfo(@Valid @ModelAttribute OtherProjectInfoAddReq reqModel, HttpSession session) {
        return operationService.addOtherProjectInfo(Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString()), reqModel.getTypeInt(), reqModel.getName(),
                reqModel.getUrl(), reqModel.getImage(), reqModel.getDes());
    }

    @ApiOperation(value = "删除某个项目记录")
    @DeleteMapping(value = "info/project/other/{id}")
    public Result<String> deleteOtherProjectInfo(@PathVariable int id, HttpSession session) {
        int accountId = Integer.parseInt(session.getAttribute(ConstValue.ACCOUNT_IDENTITY).toString());
        return operationService.deleteOtherProjectInfo(accountId, id);
    }
}
