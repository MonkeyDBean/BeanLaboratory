package com.monkeybean.socket.controller;

import com.monkeybean.socket.controller.model.ChatMessage;
import com.monkeybean.socket.model.ChatRecord;
import com.monkeybean.socket.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Created by MonkeyBean on 2018/9/6.
 */
@Controller
public class ChatController {

    @Autowired
    private ChatRecordService chatRecordService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        // insert to db
        chatRecordService.save(new ChatRecord(chatMessage.getSender(), ChatMessage.MessageType.CHAT.ordinal(), chatMessage.getContent()));
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // insert to db
        chatRecordService.save(new ChatRecord(chatMessage.getSender(), ChatMessage.MessageType.JOIN.ordinal(), "enter"));
        return chatMessage;
    }
}
