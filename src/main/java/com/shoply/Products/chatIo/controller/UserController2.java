package com.shoply.Products.chatIo.controller;

import com.shoply.Products.Model.Users;
import com.shoply.Products.chatIo.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController2 {

    @Autowired
    UserServices userServices;

    @MessageMapping("/user.connectUser")
    @SendTo("/topic/public")
    public Users connectUser(@Payload Users users){
        return userServices.saveUser(users);
    }

    @GetMapping("/connectedUsers")
    public List<Users> connectedUsers(){

        return userServices.findByStatus();
    }
}
