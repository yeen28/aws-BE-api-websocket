package com.nameless.social.api.controller;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.exception.ErrorResponse;
import com.nameless.social.api.handler.UserInfo;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import com.nameless.social.core.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;

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
//			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(userService.getUserInfo(email));
	}

	@Operation(summary = "사용자가 특정 클럽(소모임, 특정 그룹 내에 존재) 탈퇴")
	@PostMapping("/user/leaveClub")
	public CommonResponse<Object> leaveClub(
			final HttpServletRequest request,
			@RequestBody final LeaveClubDto leaveClubDto
	) {
		userService.leaveClub(leaveClubDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 그룹 탈퇴")
	@PostMapping("/user/leaveGroup")
	public CommonResponse<Object> leaveGroup(
			final HttpServletRequest request,
			@RequestBody final LeaveGroupDto leaveGroupDto
	) {
		userService.leaveGroup(leaveGroupDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 클럽 가입")
	@PostMapping("/user/joinClub")
	public CommonResponse<Object> joinClub(
			final HttpServletRequest request,
			@RequestBody final JoinClubDto joinClubDto
	) {
		userService.joinClub(joinClubDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 Group 가입")
	@PostMapping("/user/joinGroup")
	public CommonResponse<Object> joinGroup(
			final HttpServletRequest request,
			@RequestBody final JoinGroupDto joinGroupDto
	) {
		userService.joinGroup(joinGroupDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 이름 수정")
	@PostMapping("/user/{id}")
	public CommonResponse<Object> setUsername(
			final HttpServletRequest request,
			@PathVariable("id") final long id,
			@RequestBody final UsernameDto usernameDto
	) {
		userService.updateUsername(usernameDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 아바타 이미지 업로드/수정")
	@PostMapping("/user/setAvatar")
	public CommonResponse<Object> setAvatar(
			final HttpServletRequest request,
			final MultipartHttpServletRequest multipartHttpServletRequest,
			@RequestParam(value = "email", required = false) final String email
//			User user,
//			@RequestParam(value = "isDefault") boolean isDefault
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
