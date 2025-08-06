package com.nameless.social.api.controller;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
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
			@RequestParam(value = "id", required = false) String email
	) {
		return CommonResponse.success(userService.getUserByEmail(email));
	}

	@Operation(summary = "사용자 정보 조회")
	@GetMapping("/user/getUserStatus")
	public CommonResponse<UserModel> getUserStatus(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {

		// TODO 응답에 다음 값들이 포함되어야 함.
//		id: string(email)
//		name: string,
//		avatar: base64 encode + json stringify
//		status: online | offline → 확장 기능용. 현재는 그냥 “online” string으로 주세요
//		joinDate: Date(YYYY-MM-DD)
//		lastSeen: Date(YYYY-MM-DD)

		return CommonResponse.success(userService.getUserByEmail(email));
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
			@RequestParam(value = "id", required = false) String email
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/user/setUsername")
	public CommonResponse<Object> setUsername(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/user/setAvatar")
	public CommonResponse<Object> setAvatar(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@GetMapping("/group/getGroupList")
	public CommonResponse<Object> getGroupList(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {
		List<CurQuestTotalModel> curQuestTotalModelApis = List.of(CurQuestTotalModel.builder()
				.quest("questTest")
				.isSuccess(true)
				.group("GroupTest")
				.build());
		QuestModel questModelApi = QuestModel.builder()
				.id("email@test.com")
				.curQuestTotalList(curQuestTotalModelApis)
				.build();
		return CommonResponse.success(questModelApi);
	}

	@PostMapping("/group/joinUser")
	public CommonResponse<Object> joinUser(
			final HttpServletRequest request,
			@RequestParam(value = "groupname", required = false) String groupName
	) {
		return CommonResponse.success(HttpStatus.OK);
	}

	@PostMapping("/group/departUser")
	public CommonResponse<Object> departUser(
			final HttpServletRequest request,
			@RequestParam(value = "groupname", required = false) String groupName
	) {
		return CommonResponse.success(HttpStatus.OK);
	}
}
