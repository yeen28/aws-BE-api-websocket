package com.nameless.social.api.controller;

import com.nameless.social.api.dto.DepartUserDto;
import com.nameless.social.api.dto.JoinUserDto;
import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // /users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// TODO 사용자 생성을 위한 임시 코드. 추후 제거.
	@PostMapping("/users")
	public CommonResponse<UserModel> createUser(@RequestBody UserDto userDto) {
		return CommonResponse.success(userService.getOrCreateUser(userDto));
	}

	@GetMapping("/users/{id}")
	public CommonResponse<UserModel> getUserById(@PathVariable Long id) {
		return CommonResponse.success(userService.findById(id));
	}

	// TODO RESTful API로 수정
	// TODO 가능하면 API 문서 생성
	// TODO 가능하면 TDD...
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
			@RequestBody String user,
			@RequestBody String group,
			@RequestBody String club
	) {
//		hedaer: token
//		body: {
//			user: string,
//			group: string
//			club: string
//		}
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 그룹 탈퇴")
	@PostMapping("/user/leaveGroup")
	public CommonResponse<Object> leaveGroup(
			final HttpServletRequest request,
			@RequestBody  String user,
			@RequestBody String group
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
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/user/setUsername")
	public CommonResponse<Object> setUsername(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
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

	@GetMapping("/group/getGroupList")
	public CommonResponse<Object> getGroupList(final HttpServletRequest request) {
		String[] result = {"\\n  \\\"j처럼 살기\\\",\\n  \\\"0원 챌린지\\\",\\n  \\\"작심삼일\\\"\\n"};
		return CommonResponse.success(result);
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
