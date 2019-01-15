package com.monkeybean.socket.configurer;

import com.monkeybean.socket.controller.model.ChatMessage;
import com.monkeybean.socket.model.ChatRecord;
import com.monkeybean.socket.service.ChatRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Created by MonkeyBean on 2018/9/6.
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private static int CONNECTED_NUM = 0;

    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatRecordService chatRecordService;

    @Autowired
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, ChatRecordService chatRecordService) {
        this.messagingTemplate = messagingTemplate;
        this.chatRecordService = chatRecordService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection, connected num is: {} now", ++WebSocketEventListener.CONNECTED_NUM);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        if (--WebSocketEventListener.CONNECTED_NUM < 0) {
            WebSocketEventListener.CONNECTED_NUM = 0;
        }
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            logger.info("User Disconnected: {}, connected num is: {} now ", username, WebSocketEventListener.CONNECTED_NUM);

            // insert to db
            chatRecordService.save(new ChatRecord(username, ChatMessage.MessageType.LEAVE.ordinal(), "leave"));

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
