package com.example.demo.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.userName(), loginRequest.password()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtService.generateToken(userDetails);

			String role = userDetails.getAuthorities().stream()
					.map(auth -> auth.getAuthority())
					.findFirst()
					.orElse("USER");

			LoginResponse loginResponse = new LoginResponse(
					"Login successful",
					userDetails.getUsername(),
					role,
					token);

			return ResponseEntity.ok(new ApiResponse("Login successful", loginResponse));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse("Invalid username or password", null));
		}
	}

	/**
	 * Record for login request (replaces inner class).
	 */
	public record LoginRequest(String userName, String password) {
	}
}
