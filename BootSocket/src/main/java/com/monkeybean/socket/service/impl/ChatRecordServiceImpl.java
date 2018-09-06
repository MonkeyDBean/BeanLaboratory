package com.monkeybean.socket.service.impl;

import com.monkeybean.socket.core.AbstractService;
import com.monkeybean.socket.dao.ChatRecordMapper;
import com.monkeybean.socket.model.ChatRecord;
import com.monkeybean.socket.service.ChatRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by MonkeyBean on 2018/09/06.
 */
@Service
@Transactional
public class ChatRecordServiceImpl extends AbstractService<ChatRecord> implements ChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapper;

}
