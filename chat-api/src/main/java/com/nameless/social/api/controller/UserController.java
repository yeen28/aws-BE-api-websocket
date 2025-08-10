package com.nameless.social.api.controller;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.handler.UserInfo;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import com.nameless.social.core.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@Operation(summary = "Cognito 서비스가 되지 않았을 때를 위한 캐시 업데이트용")
	@GetMapping("/user/getUserCredentials")
	public CommonResponse<Object> getUserCredentials(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(userService.getUserInfo(user.getEmail()));
	}

	@Operation(summary = "사용자 정보 조회")
	@GetMapping("/user/getUserStatus")
	public CommonResponse<Object> getUserInfo(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(userService.getUserInfo(user.getEmail()));
	}

	@Operation(summary = "사용자가 특정 클럽(소모임, 특정 그룹 내에 존재) 탈퇴")
	@PostMapping("/user/leaveClub")
	public CommonResponse<Object> leaveClub(
			@UserInfo final User user,
			@RequestBody final LeaveClubDto leaveClubDto
	) {
		userService.leaveClub(user, leaveClubDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 그룹 탈퇴")
	@PostMapping("/user/leaveGroup")
	public CommonResponse<Object> leaveGroup(
			@UserInfo final User user,
			@RequestBody final LeaveGroupDto leaveGroupDto
	) {
		userService.leaveGroup(user, leaveGroupDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 클럽 가입")
	@PostMapping("/user/joinClub")
	public CommonResponse<Object> joinClub(
			@UserInfo final User user,
			@RequestBody final JoinClubDto joinClubDto
	) {
		userService.joinClub(user, joinClubDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 Group 가입")
	@PostMapping("/user/joinGroup")
	public CommonResponse<Object> joinGroup(
			@UserInfo final User user,
			@RequestBody final JoinGroupDto joinGroupDto
	) {
		userService.joinGroup(user, joinGroupDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 이름 수정")
	@PostMapping("/user/setUsername")
	public CommonResponse<Object> setUsername(
			@UserInfo final User user,
			@RequestBody final UsernameDto usernameDto
	) {
		userService.updateUsername(user, usernameDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 아바타 이미지 업로드/수정")
	@PostMapping("/user/setAvatar")
	public CommonResponse<Object> setAvatar(
			@UserInfo final User user,
			final MultipartHttpServletRequest multipartHttpServletRequest,
			@RequestParam(value = "email", required = false) final String email
//			User user,
//			@RequestParam(value = "isDefault") boolean isDefault
	) {
//		try {
		// TODO default image check
//		if (multipartHttpServletRequest != null) {
//			MultipartFile file = multipartHttpServletRequest.getFileMap().values().iterator().next();
//				if (!file.getOriginalFilename())) { // 지원하지 않는 이미지 확장자인지 체크
//					throw new IOException("Not supported image extension");
//				}
//				userRegistService.updateUserProfileImage(user.getUserId(), file);
//			} else {
//				userRegistService.deleteUserProfileImage(user.getUserId());
//		}
//		} catch (IOException e) {
//			return CommonResponse.error(ErrorResponse.of(ErrorCode.IMAGE_UPDATE_FAILED));
//		}
//		return CommonResponse.success("Updated image");
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 서비스 탈퇴")
	@DeleteMapping("/user")
	public CommonResponse<Object> deleteUser(
			@UserInfo final User user,
			@RequestParam("email") final String email
	) {
		userService.deleteUser(user.getEmail());
		return CommonResponse.success(HttpStatus.OK);
	}
}
