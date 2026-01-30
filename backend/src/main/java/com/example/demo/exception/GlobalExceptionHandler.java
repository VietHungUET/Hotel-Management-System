package com.example.demo.exception;

import com.example.demo.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Catches all exceptions and returns consistent ApiResponse format.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleAlreadyExistsException(AlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Invalid username or password", null));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("An unexpected error occurred: " + ex.getMessage(), null));
    }
}
