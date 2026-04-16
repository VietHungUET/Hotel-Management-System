package com.example.demo.dto;

public record LoginResponse(
        String message,
        String username,
        String role,
        String token
) {}
