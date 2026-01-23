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
        return userRepository.findAllByStatus(Status.ONLINE);
    }

    public Users saveUser(Users users) {
//        Updating User's status to ONLINE when they connect
        Optional<Users> users1=userRepository.findByUsername(users.getUsername());
        if (users1.isPresent()) {
            Users offlineUser = users1.get();
            offlineUser.setStatus(Status.ONLINE);
            userRepository.save(offlineUser);
        }

        return users;
    }

    public Users logoutUser(Users users) {
        Optional<Users> users1=userRepository.findByUsername(users.getUsername());
        if (users1.isPresent()) {
            Users usDb = users1.get();
            usDb.setStatus(Status.OFFLINE);
            userRepository.save(usDb);
        }

        return users;
    }
}
