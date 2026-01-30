package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper for all endpoints.
 * Provides consistent response format across the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}
