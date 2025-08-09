package com.nameless.social.api.controller;

import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
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

	@GetMapping("/user/getUserQuestWeekly")
	public CommonResponse<Object> getUserQuestWeekly(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {

//		id: string(email)
//		weeklyQuestList: […{
//			day: number
//			questTotalNum: number,
//			successQuestNum: number,
//			bestParticipateGroup: string
//		}]

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

	// TODO token에 사용자에 대한 정보가 있으니깐 email을 전달하지 않아도 됨.
	@GetMapping("/user/getUserQuestPrev")
	public CommonResponse<QuestModel> getUserQuestPrev(
			final HttpServletRequest request,
			@RequestParam(value = "email", required = false) String email
	) {

//		id: string(email)
//		prevQuestTotalList: […{
//			quest: string,
//			group: string,
//			isSuccess: boolean,
//			completeTime: Date(YYYY-MM-DD)
//		}]

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

	@PostMapping("/user/setUserQuestRecord")
	public CommonResponse<Object> setUserQuestRecord(
			final HttpServletRequest request,
			@RequestBody String[] userQuest
	) {
//		header: token
//		body : {
//			userQuest: string[]
//		}
		// TODO service에서 setUserQuestRecord 수행
		return CommonResponse.success(HttpStatus.OK);
	}
}
