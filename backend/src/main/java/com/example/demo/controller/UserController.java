package com.example.demo.controller;

import java.util.HashMap;

import com.example.demo.entity.UserAccount;
import com.example.demo.service.CustomDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.UserAccountRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserService.UserInput;
import com.example.demo.entity.User;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping(value="/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			String validationCode = userService.initiateRegistration(user);
			return ResponseEntity.ok("Validation code sent to email. Code: " + validationCode);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@PostMapping(value="/register/validation")
	public ResponseEntity<?> validateRegistration(@RequestBody ValidationRequest request ) {
		try {
			User registeredUser = userService.completeRegistration(request.getValidationCode(), request.getUser());
			return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful. User ID: " + registeredUser.getId());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}




//	@PostMapping(value = "/register/validation")
//	public ResponseEntity<?> validation(@RequestBody UserInput user_input) {
//		String input = user_input.userInput();
//		System.out.println("user_input:" + input);
//		if (check_valid.containsKey(input)) {
//			User u = check_valid.get(input);
//			check_valid.remove(input);
//			userService.saveDetails(u);
//			return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký tài khoản thành công");
//		}
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mã xác thực không tồn tại");
//	}
	

	


	@DeleteMapping(value = "/admin/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		try {
			userService.removeUserById(id);
			return ResponseEntity.ok("User deleted successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping(value = "/home")
	public ResponseEntity<?> homepage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login to access this page");
		}
		String username = authentication.getName();
		String role = authentication.getAuthorities().stream()
				.map(auth -> auth.getAuthority())
				.findFirst()
				.orElse("USER");
		// Trả về JSON với các trường username và role
		return ResponseEntity.ok(new HashMap<String,Object>() {{
			put("username", username);
			put("role", role);
		}});
	}
	
	@GetMapping(value = "/admin")
	public ResponseEntity<?> admin_notify() {
		
		return ResponseEntity.status(HttpStatus.OK).body("Hello admin!");
	}
	
	@GetMapping(value = "/manager")
	public ResponseEntity<?> manager_notify() {
		
		return ResponseEntity.status(HttpStatus.OK).body("Hello manager!");
	}
	
	@GetMapping(value = "/receptionist")
	public ResponseEntity<?> receptionist_notify() {
		
		return ResponseEntity.status(HttpStatus.OK).body("Hello receptionist!");
	}

	public static class ValidationRequest {
		private String validationCode;
		private User user;

		public String getValidationCode() {
			return validationCode;
		}

		public void setValidationCode(String validationCode) {
			this.validationCode = validationCode;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}

}
