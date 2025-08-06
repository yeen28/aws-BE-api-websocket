package com.nameless.social.api.controller;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// TODO 사용자 생성을 위한 임시 코드. 추후 제거.
	@PostMapping("/user")
	public CommonResponse<UserModel> createUser(@RequestBody UserDto userDto) {
		return CommonResponse.success(userService.getOrCreateUser(userDto));
	}

	@GetMapping("/user/{id}")
	public CommonResponse<UserModel> getUserById(@PathVariable("id") Long id) {
		return CommonResponse.success(userService.findById(id));
	}

	@GetMapping("/users")
	public CommonResponse<Object> getUsers() {
		return CommonResponse.success(userService.findAll());
	}

	// TODO RESTful API로 수정
	@Operation(summary = "Cognito 서비스가 되지 않았을 때를 위한 캐시 업데이트용")
	@GetMapping("/user/getUserCredentials")
	public CommonResponse<Object> getUserCredentials(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(userService.getUserInfo(email));
	}

	@Operation(summary = "사용자 정보 조회")
	@GetMapping("/user/getUserStatus")
	public CommonResponse<Object> getUserInfo(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(userService.getUserInfo(email));
	}

	@Operation(summary = "사용자가 특정 클럽(소모임, 특정 그룹 내에 존재) 탈퇴")
	@PostMapping("/user/leaveClub")
	public CommonResponse<Object> leaveClub(
			final HttpServletRequest request,
			@RequestBody final LeaveClubDto leaveClubDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 그룹 탈퇴")
	@PostMapping("/user/leaveGroup")
	public CommonResponse<Object> leaveGroup(
			final HttpServletRequest request,
			@RequestBody final LeaveGroupDto leaveGroupDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 클럽 가입")
	@PostMapping("/user/joinClub")
	public CommonResponse<Object> joinClub(
			final HttpServletRequest request,
			@RequestBody String info
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/user/joinGroup")
	public CommonResponse<Object> joinGroup(
			final HttpServletRequest request,
			@RequestBody JoinClubDto joinClubDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 이름 수정")
	@PostMapping("/user/{id}")
	public CommonResponse<Object> setUsername(
			final HttpServletRequest request,
			@PathVariable("id") final long id,
			@RequestBody final UsernameDto usernameDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/user/setAvatar")
	public CommonResponse<Object> setAvatar(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/group/joinUser")
	public CommonResponse<Object> joinUser(
			final HttpServletRequest request,
			@RequestBody JoinUserDto joinUserDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/group/departUser")
	public CommonResponse<Object> departUser(
			final HttpServletRequest request,
			@RequestBody DepartUserDto departUserDto
	) {
		return CommonResponse.success(HttpStatus.OK);
	}
}
