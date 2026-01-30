package com.example.demo.controller;

import com.example.demo.request.CreateUserRequest;
import com.example.demo.request.ValidationRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> register(@RequestBody CreateUserRequest request) {
		// Convert DTO to Entity
		User user = new User();
		user.setFullName(request.getFullName());
		user.setUserName(request.getUserName());
		user.setPhone(request.getPhone());
		user.setEmail(request.getEmail());
		user.setUserPassword(request.getPassword());
		user.setRole(request.getRole());

		String validationCode = userService.initiateRegistration(user);
		return ResponseEntity.ok(new ApiResponse("Validation code sent to email", validationCode));
	}

	@PostMapping("/register/validate")
	public ResponseEntity<ApiResponse> validateRegistration(@RequestBody ValidationRequest request) {
		// Convert DTO to Entity
		User user = new User();
		user.setFullName(request.getUser().getFullName());
		user.setUserName(request.getUser().getUserName());
		user.setPhone(request.getUser().getPhone());
		user.setEmail(request.getUser().getEmail());
		user.setUserPassword(request.getUser().getPassword());
		user.setRole(request.getUser().getRole());

		User registeredUser = userService.completeRegistration(request.getValidationCode(), user);
		UserDto userDto = userService.convertToDto(registeredUser);
		return ResponseEntity.ok(new ApiResponse("Registration successful", userDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable int id) {
		userService.removeUserById(id);
		return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse> getAllUsers() {
		List<User> users = userService.getAllUsers();
		List<UserDto> userDtos = userService.getConvertedUsers(users);
		return ResponseEntity.ok(new ApiResponse("Users retrieved successfully", userDtos));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable int id) {
		User user = userService.getUserById(id);
		UserDto userDto = userService.convertToDto(user);
		return ResponseEntity.ok(new ApiResponse("User retrieved successfully", userDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable int id, @RequestBody User user) {
		User updatedUser = userService.updateUser(id, user);
		UserDto userDto = userService.convertToDto(updatedUser);
		return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication.getPrincipal().equals("anonymousUser")) {
			return ResponseEntity.status(401).body(new ApiResponse("Please login to access this page", null));
		}

		String username = authentication.getName();
		String role = authentication.getAuthorities().stream()
				.map(auth -> auth.getAuthority())
				.findFirst()
				.orElse("USER");

		Map<String, Object> userData = new HashMap<>();
		userData.put("username", username);
		userData.put("role", role);

		return ResponseEntity.ok(new ApiResponse("User info retrieved successfully", userData));
	}
}
