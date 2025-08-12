package com.nameless.social.api.controller;

import com.nameless.social.api.dto.QuestSuccessDto;
import com.nameless.social.api.dto.UserQuestDto;
import com.nameless.social.api.handler.UserInfo;
import com.nameless.social.api.model.*;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.QuestService;
import com.nameless.social.core.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestController {
	private final QuestService questService;

	@Operation(summary = "현재 진행하고 있는 퀘스트들 목록")
	@GetMapping("/user/getUserQuestCur")
	public CommonResponse<QuestModel> getQuest(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getQuest(user.getEmail()));
	}

	@Operation(summary = "30일 기준으로 연속해서 성공하고 있는 퀘스트 수")
	@GetMapping("/user/getUserQuestContinuous")
	public CommonResponse<QuestContinuousModel> getQuestStatisticsByUser(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) String email
	) {
		return CommonResponse.success(questService.getQuestStatisticsByUser(user));
	}

	@Operation(summary = "사용자가 성공한 퀘스트를 요일별로 집산")
	@GetMapping("/user/getUserQuestWeekly")
	public CommonResponse<UserQuestWeeklyModel> getUserQuestWeekly(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getUserQuestWeekly(user.getEmail()));
	}

	@Operation(summary = "오늘 이전에 할당받은 퀘스트 수행 여부 집산")
	@GetMapping("/user/getUserQuestPrev")
	public CommonResponse<UserQuestPrevModel> getUserQuestPrev(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getUserQuestPrev(user.getEmail()));
	}

	@Operation(summary = "퀘스트 수행 완료 버튼 클릭 시 퀘스트 수행 여부 최신화")
	@PostMapping("/user/setUserQuestRecord")
	public CommonResponse<Object> setUserQuestRecord(
			@UserInfo final User user,
			@RequestBody final UserQuestDto userQuestDto
	) {
		questService.setUserQuestRecord(user, userQuestDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "퀘스트 성공 처리")
	@PostMapping("/group/questSuccess")
	public CommonResponse<Object> questSuccess(
			@UserInfo final User user,
			@RequestBody final QuestSuccessDto questSuccessDto
	) {
		questService.questSuccess(user, questSuccessDto);
		return CommonResponse.success(HttpStatus.OK);
	}
}
