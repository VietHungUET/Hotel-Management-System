package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.CustomDetails; // Vẫn cần CustomDetails nếu bạn dùng nó cho các thông tin khác
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService.LoginRequest;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.user_name(), loginRequest.user_password())
			);

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtService.generateToken(userDetails);

			String role = userDetails.getAuthorities().stream()
					.map(auth -> auth.getAuthority())
					.findFirst()
					.orElse("USER");


			// Cập nhật LoginResponse constructor để không còn hotelId
			return ResponseEntity.ok(new LoginResponse("Đăng nhập thành công!", userDetails.getUsername(), role, token));

		} catch (Exception e) {
			System.err.println("Authentication failed: " + e.getMessage());
			// Cập nhật LoginResponse constructor để không còn hotelId
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponse("Tài khoản hoặc mật khẩu không chính xác", null, null, null));
		}
	}

	public static class LoginResponse {
		private String message;
		private String username;
		private String role;
		private String token;

		// Cập nhật constructor
		public LoginResponse(String message, String username, String role, String token) {
			this.message = message;
			this.username = username;
			this.role = role;
			this.token = token;
		}

		public String getMessage() { return message; }
		public String getUsername() { return username; }
		public String getRole() { return role; }

		public String getToken() { return token; }
	}

}
