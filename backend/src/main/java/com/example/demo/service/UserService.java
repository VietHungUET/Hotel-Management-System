package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAccountRepository userAccRepo;

    public boolean check_existed(String userAcc) {
        Optional<UserAccount> userOptional = userAccRepo.findByUserName(userAcc);

        if (userOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public User saveDetails(User user) {
        BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
        String encryptedPass = crypt.encode(user.getUser_password());
        user.setUser_password(encryptedPass);
        UserAccount acc = new UserAccount(user.getUser_name(), user.getUser_password(), true, "MANAGER", user.getId());
        userAccRepo.save(acc);
        return userRepo.save(user);
    }

    public void removeUserById(int id) {
        userRepo.deleteById(id);
    }

    public User getUserById(int id) {
        return userRepo.getReferenceById(id);
    }
    public record UserRequest(String full_name, String user_name, String phone, String email, String user_password) {

    }

    public record LoginRequest(String user_name, String user_password) {

    }

    public record UserInput(String userInput) {

    }

    public record AddAccount(String user_name, String user_password, boolean active, String role) {

    }
}
