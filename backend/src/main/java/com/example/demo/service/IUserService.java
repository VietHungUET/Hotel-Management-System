package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

import java.util.List;

/**
 * Interface for user service operations.
 */
public interface IUserService {

    // CRUD operations
    User getUserById(int id);

    List<User> getAllUsers();

    User findByUsername(String username);

    void removeUserById(int id);

    // Registration
    boolean checkExisted(String userName);

    String initiateRegistration(User user);

    User completeRegistration(String uuid, User user);

    // Account management
    User addAccount(String userName, String password, String role, String email, String fullName, String phone);

    User updateUser(int id, User user);

    // DTO conversion
    UserDto convertToDto(User user);

    List<UserDto> getConvertedUsers(List<User> users);
}
