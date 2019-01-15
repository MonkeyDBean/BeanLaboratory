package com.monkeybean.socket.service;

import com.monkeybean.socket.core.Service;
import com.monkeybean.socket.model.ChatRecord;

import java.util.List;

/**
 * Created by MonkeyBean on 2018/09/06.
 */
public interface ChatRecordService extends Service<ChatRecord> {
    List<ChatRecord> findRecordByUserName(String name, int pageNum, int pageSize);
}
