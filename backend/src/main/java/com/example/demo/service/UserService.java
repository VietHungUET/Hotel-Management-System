package com.example.demo.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepo;
//    private final UserAccountRepository userAccRepo;
    private final EmailSenderService emailService;

    public boolean check_existed(String userName) {
        return userRepo.findByUserName(userName).isPresent();
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

            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("user"); // Đặt ROLE mặc định là "USER"
            }

            User savedUser = userRepo.save(user);
            return savedUser;
        }
        throw new RuntimeException("Invalid validation code");
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepo.findByUserName(username);
        return user.orElse(null); // Trả về null nếu không tìm thấy
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

    public User addAccount(String userName, String password, String role, String email, String fullName, String phone) {
        if (check_existed(userName)) {
            throw new RuntimeException("Username already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass = encoder.encode(password);

        User newUser = new User();
        newUser.setUser_name(userName);
        newUser.setUser_password(encryptedPass);
        newUser.setRole(role);
        newUser.setEmail(email);
        newUser.setFull_name(fullName);
        newUser.setPhone(phone);

        return userRepo.save(newUser);
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
