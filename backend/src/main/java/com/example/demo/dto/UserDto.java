package com.example.demo.dto;

/**
 * DTO for user response (excludes password for security).
 */
public record UserDto(
        Integer id,
        String fullName,
        String userName,
        String phone,
        String email,
        String role
) {}
