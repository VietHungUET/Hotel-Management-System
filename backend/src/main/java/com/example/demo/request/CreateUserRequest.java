package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String fullName;
    private String userName;
    private String phone;
    private String email;
    private String password;
    private String role;
}
