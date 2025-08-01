package com.nameless.social.api.controller;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.model.user.UserModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// TODO 사용자 생성을 위한 임시 코드. 추후 제거.
	@PostMapping
	public CommonResponse<UserModel> createUser(@RequestBody UserDto userDto) {
		return CommonResponse.success(userService.getOrCreateUser(userDto));
	}

	@GetMapping("/{id}")
	public CommonResponse<UserModel> getUserById(@PathVariable Long id) {
		return CommonResponse.success(userService.findById(id));
	}

	// TODO: Add endpoint for user registration/login using AWS Cognito
}
