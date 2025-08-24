package com.nameless.social.api.controller;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.handler.UserInfo;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import com.nameless.social.core.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@Operation(summary = "Cognito 서비스가 되지 않았을 때를 위한 캐시 업데이트용")
	@GetMapping("/credentials")
	public CommonResponse<Object> getUserCredentials(@UserInfo final User user) {
		return CommonResponse.success(userService.getUserInfo(user.getEmail()));
	}

	@Operation(summary = "내 정보 조회")
	@GetMapping("/status")
	public CommonResponse<Object> getUserInfo(@UserInfo final User user) {
		return CommonResponse.success(userService.getUserInfo(user.getEmail()));
	}

	@Operation(summary = "사용자가 특정 클럽(소모임, 특정 그룹 내에 존재) 탈퇴")
	@DeleteMapping("/club/{clubId}")
	public CommonResponse<Object> leaveClub(
			@UserInfo final User user,
			@PathVariable("clubId") final long clubId
	) {
		userService.leaveClub(user, clubId);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 특정 그룹 탈퇴")
	@DeleteMapping("/group/{groupId}")
	public CommonResponse<Object> leaveGroup(
			@UserInfo final User user,
			@PathVariable("groupId") final long groupId
	) {
		userService.leaveGroup(user, groupId);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 클럽(하나 또는 여러 개) 가입")
	@PostMapping("/club")
	public CommonResponse<Object> joinClubs(
			@UserInfo final User user,
			@RequestBody final JoinClubDto joinClubDto
	) {
		userService.joinClubs(user, joinClubDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자가 하나의 Group 가입")
	@PostMapping("/group/{grouopId}")
	public CommonResponse<Object> joinGroup(
			@UserInfo final User user,
			@PathVariable("groupId") final long groupId
	) {
		userService.joinGroup(user, groupId);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 이름 수정")
	@PutMapping("/name")
	public CommonResponse<Object> setUsername(
			@UserInfo final User user,
			@RequestBody final UsernameDto usernameDto
	) {
		userService.updateUsername(user, usernameDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "사용자 아바타 이미지 업로드/수정")
	@PutMapping("/avatar")
	public CommonResponse<Object> setAvatar(
			@UserInfo final User user,
			final MultipartHttpServletRequest multipartHttpServletRequest,
			@RequestParam(value = "isDefault") final boolean isDefault
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
	public CommonResponse<Object> deleteUser(@UserInfo final User user) {
		userService.deleteUser(user.getEmail());
		return CommonResponse.success(HttpStatus.OK);
	}
}
