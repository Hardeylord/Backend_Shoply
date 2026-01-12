package com.shoply.Products.chatIo.controller;

import com.shoply.Products.chatIo.chatModel.ChatMessages;
import com.shoply.Products.chatIo.services.ChatMessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatRoomController {

    @Autowired
    ChatMessageServices chatMessageServices;
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessages chatMessages){
       var savedChats= chatMessageServices.saveChats(chatMessages);

       messagingTemplate.convertAndSendToUser(chatMessages.getRecipientId(), "/messages", savedChats);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessages>> findChatMessages(@PathVariable("senderId") String senderId,
                                                             @PathVariable("recipientId") String recipientId) {

       return ResponseEntity.ok(chatMessageServices.findChatMessages(senderId, recipientId));
    }
}
