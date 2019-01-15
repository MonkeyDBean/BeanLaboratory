package com.monkeybean.socket.service.impl;

import com.github.pagehelper.PageHelper;
import com.monkeybean.socket.core.AbstractService;
import com.monkeybean.socket.dao.ChatRecordMapper;
import com.monkeybean.socket.model.ChatRecord;
import com.monkeybean.socket.service.ChatRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/09/06.
 */
@Service
@Transactional
public class ChatRecordServiceImpl extends AbstractService<ChatRecord> implements ChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapper;

    /**
     * 通过用户名查记录
     *
     * @param name     用户名
     * @param pageNum  页序号,初始为1
     * @param pageSize 每页记录数
     */
    public List<ChatRecord> findRecordByUserName(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Condition condition = new Condition(ChatRecord.class);
        condition.orderBy("id").desc();
        condition.createCriteria().andEqualTo("userName", name);
        return chatRecordMapper.selectByCondition(condition);
    }

}
