package com.monkeybean.socket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.monkeybean.socket.core.Result;
import com.monkeybean.socket.core.ResultGenerator;
import com.monkeybean.socket.model.ChatRecord;
import com.monkeybean.socket.service.ChatRecordService;
import com.monkeybean.socket.vo.ChatRecordVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/09/06.
 */
@RestController
@RequestMapping("/chat/record")
public class ChatRecordController {
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Resource
    private ChatRecordService chatRecordService;

    @GetMapping("/list")
    @SuppressWarnings("unchecked")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, @RequestParam(required = false) String name) {
        List<ChatRecord> list;
        if (StringUtils.isEmpty(name)) {
            PageHelper.startPage(page, size);
            list = chatRecordService.findAll();
        } else {
            list = chatRecordService.findRecordByUserName(name, page, size);
        }
        PageInfo pageInfo = new PageInfo(list);
        List<ChatRecord> recordList = pageInfo.getList();
        List<ChatRecordVo> dataList = new ArrayList<>();
        for (ChatRecord eachRecord : recordList) {
            ChatRecordVo chatRecordVo = new ChatRecordVo();
            BeanUtils.copyProperties(eachRecord, chatRecordVo);
            chatRecordVo.setGenerateTime(new DateTime(eachRecord.getCreateTime()).toString(STANDARD_FORMAT));
            dataList.add(chatRecordVo);
        }
        pageInfo.setList(dataList);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
