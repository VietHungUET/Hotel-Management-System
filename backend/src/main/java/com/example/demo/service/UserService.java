package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepo;
    private final EmailSenderService emailService;

    @Override
    public boolean checkExisted(String userName) {
        return userRepo.findByUserName(userName).isPresent();
    }

    @Override
    public String initiateRegistration(User user) {
        if (checkExisted(user.getUserName())) {
            throw new AlreadyExistsException("User", "username", user.getUserName());
        }
        return emailService.sendAuthenticationEmail(user.getEmail());
    }

    @Override
    public User completeRegistration(String uuid, User user) {
        if (!emailService.validateCode(uuid)) {
            throw new BadRequestException("Invalid validation code");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass = encoder.encode(user.getUserPassword());
        user.setUserPassword(encryptedPass);

        // Normalize role to uppercase
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }

        return userRepo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public void removeUserById(int id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public User getUserById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User addAccount(String userName, String password, String role, String email, String fullName, String phone) {
        if (checkExisted(userName)) {
            throw new AlreadyExistsException("User", "username", userName);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass = encoder.encode(password);

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserPassword(encryptedPass);
        newUser.setRole(role);
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPhone(phone);

        return userRepo.save(newUser);
    }

    @Override
    public User updateUser(int id, User user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (user.getFullName() != null) {
            existingUser.setFullName(user.getFullName());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getUserPassword() != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            existingUser.setUserPassword(encoder.encode(user.getUserPassword()));
        }

        return userRepo.save(existingUser);
    }

    @Override
    public UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getUserName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole());
    }

    @Override
    public List<UserDto> getConvertedUsers(List<User> users) {
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
