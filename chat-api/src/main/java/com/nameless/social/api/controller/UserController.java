package com.nameless.social.api.controller;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping // ("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// TODO 사용자 생성을 위한 임시 코드. 추후 제거.
	@PostMapping("/api/users")
	public CommonResponse<UserModel> createUser(@RequestBody UserDto userDto) {
		return CommonResponse.success(userService.getOrCreateUser(userDto));
	}

	@GetMapping("/{id}")
	public CommonResponse<UserModel> getUserById(@PathVariable Long id) {
		return CommonResponse.success(userService.findById(id));
	}

	// TODO: Add endpoint for user registration/login using AWS Cognito

	@GetMapping("/api/user/getUserJoinList")
	public CommonResponse<UserModelApi> getUserJoinList(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {
		List<JoinListModelApi> joinListModelApis = List.of(JoinListModelApi.builder()
				.groupname("testGroup")
				.clubList(new String[]{"testClub"})
				.build());
		UserModelApi userModelApi = UserModelApi.builder()
				.id("email@test.com")
				.joinList(joinListModelApis)
				.build();
		return CommonResponse.success(userModelApi);
	}

	@GetMapping("/api/user/getUserQuestCur")
	public CommonResponse<QuestModelApi> getQuest(@RequestParam(value = "id", required = false) String email) {
		List<CurQuestTotalModelApi> curQuestTotalModelApis = List.of(CurQuestTotalModelApi.builder()
				.quest("questTest")
				.isSuccess(true)
				.group("GroupTest")
				.build());
		QuestModelApi questModelApi = QuestModelApi.builder()
				.id("email@test.com")
				.curQuestTotalList(curQuestTotalModelApis)
				.build();
		return CommonResponse.success(questModelApi);
	}

	@GetMapping("api/user/getUserQuestContinuous")
	public CommonResponse<QuestModelApi> get(
			final HttpServletRequest request,
			@RequestParam(value = "id", required = false) String email
	) {
		List<CurQuestTotalModelApi> curQuestTotalModelApis = List.of(CurQuestTotalModelApi.builder()
				.quest("questTest")
				.isSuccess(true)
				.group("GroupTest")
				.build());
		QuestModelApi questModelApi = QuestModelApi.builder()
				.id("email@test.com")
				.curQuestTotalList(curQuestTotalModelApis)
				.build();
		return CommonResponse.success(questModelApi);
	}
}
