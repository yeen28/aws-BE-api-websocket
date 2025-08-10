package com.nameless.social.api.controller;

import com.nameless.social.api.dto.QuestSuccessDto;
import com.nameless.social.api.dto.UserQuestDto;
import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
import com.nameless.social.api.model.UserQuestPrevModel;
import com.nameless.social.api.model.UserQuestWeeklyModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.QuestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getQuest(email));
	}

	// TODO 굳이 API로 내려줘야 하나 생각해보기. FE에서 가지고 있는 값으로 처리 못하나 확인 필요
	@Operation(summary = "연속으로 성공하고 있는 퀘스트 수")
	@GetMapping("/user/getUserQuestContinuous")
	public CommonResponse<QuestModel> getUserQuestContinuous(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {
		List<CurQuestTotalModel> curQuestTotalModelApis = List.of(CurQuestTotalModel.builder()
				.quest("questTest")
				.isSuccess(true)
				.group("건강")
				.build());
		QuestModel questModelApi = QuestModel.builder()
				.id("email@test.com")
				.curQuestTotalList(curQuestTotalModelApis)
				.build();
		return CommonResponse.success(questModelApi);
	}

	@Operation(summary = "사용자가 성공한 퀘스트를 요일별로 집산")
	@GetMapping("/user/getUserQuestWeekly")
	public CommonResponse<UserQuestWeeklyModel> getUserQuestWeekly(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getUserQuestWeekly(email));
	}

	@Operation(summary = "오늘 이전에 할당받은 퀘스트 수행 여부 집산")
	@GetMapping("/user/getUserQuestPrev")
	public CommonResponse<UserQuestPrevModel> getUserQuestPrev(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(questService.getUserQuestPrev(email));
	}

	@Operation(summary = "퀘스트 수행 완료 버튼 클릭 시 퀘스트 수행 여부 최신화")
	@PostMapping("/user/setUserQuestRecord")
	public CommonResponse<Object> setUserQuestRecord(
			final HttpServletRequest request,
			@RequestBody UserQuestDto userQuestDto
	) {
		questService.setUserQuestRecord(userQuestDto);
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "")
	@PostMapping("/group/questSuccess")
	public CommonResponse<Object> questSuccess(
			@RequestBody QuestSuccessDto questSuccessDto
	) {
		questService.questSuccess(questSuccessDto);
		return CommonResponse.success(HttpStatus.OK);
	}
}
