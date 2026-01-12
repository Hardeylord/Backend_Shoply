package com.shoply.Products.chatIo.services;

import com.shoply.Products.chatIo.chatModel.ChatMessages;
import com.shoply.Products.chatIo.repo.ChatMessagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServices {

    @Autowired
    ChatMessagesRepo chatMessagesRepo;
    @Autowired
    ChatRoomServices chatRoomServices;

    public List<ChatMessages> findChatMessages(String senderId, String recipientId) {

       var chatId = chatRoomServices.findChatRoomId(senderId, recipientId);

        return chatId.map(chatMessagesRepo::findByChatId).orElse(new ArrayList<>());
    }

    public ChatMessages saveChats(ChatMessages chatMessages) {
        String chatId = chatRoomServices.findChatRoomId(chatMessages.getSenderId(), chatMessages.getRecipientId())
                .orElseThrow(()-> new RuntimeException("No ChatRoom"));
        chatMessages.setChatId(chatId);
        chatMessagesRepo.save(chatMessages);

        return chatMessages;
    }
}
