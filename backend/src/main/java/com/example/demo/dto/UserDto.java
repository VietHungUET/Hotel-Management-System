package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user response (excludes password for security).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String fullName;
    private String userName;
    private String phone;
    private String email;
    private String role;
}
