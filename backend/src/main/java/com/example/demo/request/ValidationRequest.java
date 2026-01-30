package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for validation request during registration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationRequest {
    private String validationCode;
    private CreateUserRequest user;
}
