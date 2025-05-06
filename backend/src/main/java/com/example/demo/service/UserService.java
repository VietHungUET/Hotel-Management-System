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

    @Autowired
    private EmailSenderService emailService;

    public boolean check_existed(String userName) {
        return userAccRepo.findByUserName(userName).isPresent();
    }

    public String initiateRegistration(User user) {
        if (check_existed(user.getUser_name())) {
            throw new RuntimeException("Username already exists");
        }
        String uuid = emailService.sendAuthenticationEmail(user.getEmail());
        return uuid;
    }

    public User completeRegistration(String uuid, User user) {
        // Giả định validateCode kiểm tra uuid (cần thêm bảng hoặc cache để lưu uuid)
        if (emailService.validateCode(uuid)) { // Cần triển khai validateCode trong EmailSenderService
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encryptedPass = encoder.encode(user.getUser_password());
            user.setUser_password(encryptedPass);
            User savedUser = userRepo.save(user);
            UserAccount account = new UserAccount(user.getUser_name(), encryptedPass, true, "MANAGER", savedUser.getId());
            userAccRepo.save(account);
            return savedUser;
        }
        throw new RuntimeException("Invalid validation code");
    }

    public UserAccount findByUsername(String username) {
        Optional<UserAccount> userAccount = userAccRepo.findByUserName(username);
        return userAccount.orElse(null); // Trả về null nếu không tìm thấy
    }


//    public User saveDetails(User user) {
//        BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();
//        String encryptedPass = crypt.encode(user.getUser_password());
//        user.setUser_password(encryptedPass);
//        UserAccount acc = new UserAccount(user.getUser_name(), user.getUser_password(), true, "MANAGER", user.getId());
//        userAccRepo.save(acc);
//        return userRepo.save(user);
//    }

    public void removeUserById(int id) {
        userRepo.deleteById(id);
    }

    public User getUserById(int id) {
        return userRepo.getReferenceById(id);
    }

    public UserAccount addAccount(String userName, String password, boolean active, String role, int hotelId) {
        if (check_existed(userName)) {
            throw new RuntimeException("Username already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass = encoder.encode(password);
        UserAccount account = new UserAccount(userName, encryptedPass, active, role, hotelId);
        return userAccRepo.save(account);
    }

    public User updateUser(int id, User user) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getFull_name() != null) existingUser.setFull_name(user.getFull_name());
        if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
        if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if (user.getUser_password() != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            existingUser.setUser_password(encoder.encode(user.getUser_password()));
        }
        return userRepo.save(existingUser);
    }
    public record UserRequest(String full_name, String user_name, String phone, String email, String user_password) {

    }

    public record LoginRequest(String user_name, String user_password) {

    }

    public record UserInput(String validationCode) {

    }

    public record AddAccount(String user_name, String user_password, boolean active, String role) {

    }


}
