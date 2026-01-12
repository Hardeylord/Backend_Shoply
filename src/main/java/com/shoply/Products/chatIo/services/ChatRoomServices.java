package com.shoply.Products.chatIo.services;

import com.shoply.Products.chatIo.chatModel.ChatRoom;
import com.shoply.Products.chatIo.repo.ChatMessagesRepo;
import com.shoply.Products.chatIo.repo.ChatRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomServices {

    @Autowired
    ChatMessagesRepo chatMessagesRepo;

    @Autowired
    ChatRoomRepo chatRoomRepo;

    public Optional<String> findChatRoomId(String senderId, String recipientId) {

       return chatRoomRepo.findBySenderIdAndRecipientId(senderId, recipientId)
               .map(ChatRoom::getChatId).or(()->{
           String chatId=createChatRoom(senderId,recipientId);
           return Optional.of(chatId);
       });
    }

    private String createChatRoom(String senderId, String recipientId) {
        var chatId=String.format("%s_%s", senderId, recipientId);

        ChatRoom viaSender = new ChatRoom();
        viaSender.setChatId(chatId);
        viaSender.setSenderId(senderId);
        viaSender.setRecipientId(recipientId);

        ChatRoom viaRecipient = new ChatRoom();
        viaRecipient.setChatId(chatId);
        viaRecipient.setSenderId(recipientId);
        viaRecipient.setRecipientId(senderId);

        chatRoomRepo.save(viaSender);
        chatRoomRepo.save(viaRecipient);

        return chatId;
    }
}
