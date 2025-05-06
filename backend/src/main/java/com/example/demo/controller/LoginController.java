package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	public LoginController(AuthenticationManager authenticationManager,UserService userService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// Xác thực người dùng
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					loginRequest.user_name(), loginRequest.user_password());
			Authentication authentication = authenticationManager.authenticate(token);

			// Lưu thông tin xác thực vào SecurityContext và HttpSession
			SecurityContext context = securityContextHolderStrategy.createEmptyContext();
			context.setAuthentication(authentication);
			securityContextHolderStrategy.setContext(context);
			securityContextRepository.saveContext(context, request, response);

			String username = loginRequest.user_name();
			String role = authentication.getAuthorities().stream()
					.map(auth -> auth.getAuthority())
					.findFirst()
					.orElse("USER");
			UserAccount user = userService.findByUsername(username);
			Integer hotelId = (user != null) ? user.getHotelId() : null;

			return ResponseEntity.ok(new LoginResponse("Đăng nhập thành công!", username, role,hotelId));
		} catch (Exception e) {
			System.out.print(e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponse("Tài khoản hoặc mật khẩu không chính xác", null, null,null));
		}
	}

	public static class LoginResponse {
		private String message;
		private String username;
		private String role;
		private Integer hotelId;

		public LoginResponse(String message, String username, String role, Integer hotelId) {
			this.message = message;
			this.username = username;
			this.role = role;
			this.hotelId = hotelId;
		}

		public String getMessage() {
			return message;
		}

		public String getUsername() {
			return username;
		}

		public String getRole() {
			return role;
		}
		public Integer getHotelId() { return hotelId; }
	}

}