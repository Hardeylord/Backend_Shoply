package com.shoply.Products.chatIo.services;

import com.shoply.Products.Model.Status;
import com.shoply.Products.Model.Users;
import com.shoply.Products.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;
    public List<Users> findByStatus() {

//        List<Users> online = userRepository.findAllByStatus(Status.ONLINE);
//        for (int i = 0; i < online.size(); i++) {
//            System.out.println(online.get(i));
//        }
        return userRepository.findAllByStatus(Status.ONLINE);
    }

    public Users saveUser(Users users) {
//        Updating User's status to ONLINE when they connect
        Optional<Users> users1=userRepository.findByUsername(users.getUsername());
        if (users1.isPresent()) {
            users.setStatus(Status.ONLINE);
            userRepository.save(users);
        }

        return users;
    }
}
