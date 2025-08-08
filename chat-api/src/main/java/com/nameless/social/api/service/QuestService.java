package com.nameless.social.api.service;

import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
	// TODO Quest는 DynamoDB에 저장되기 때문에 어떻게 처리할지 다시 확인 필요

	public QuestModel getQuest(final String email) {
		List<CurQuestTotalModel> curQuestTotalModelApis = List.of(CurQuestTotalModel.builder()
				.quest("questTest")
				.isSuccess(true)
				.group("건강")
				.build());
		return QuestModel.builder()
				.id(email)
				.curQuestTotalList(curQuestTotalModelApis)
				.build();
	}
}
