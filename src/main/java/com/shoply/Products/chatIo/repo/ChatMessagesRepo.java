package com.shoply.Products.chatIo.repo;

import com.shoply.Products.chatIo.chatModel.ChatMessages;
import com.shoply.Products.chatIo.chatModel.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessagesRepo extends MongoRepository<ChatMessages, String> {

    List<ChatMessages> findByChatId(String chatId);


}
