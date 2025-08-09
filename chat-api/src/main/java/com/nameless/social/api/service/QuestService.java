package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.QuestRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
	private final UserRepository userRepository;
	private final UserGroupRepository userGroupRepository;

	public QuestModel getQuest(final String email) {
		// 1. 유저 조회
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 2. 유저가 속한 그룹 리스트 조회
		List<Group> groups = userGroupRepository.findAllByIdUserId(user.getId())
				.stream()
				.map(UserGroup::getGroup)
				.toList();

		// 3. questMap: questId -> {Group, Club} 매핑, allQuests: 모든 Quest 수집
		Map<Long, Map<Group, Club>> questMap = new HashMap<>();
		List<Quest> allQuests = new ArrayList<>();

		for (Group group : groups) {
			for (Club club : group.getClubs()) {
				for (Quest quest : club.getQuest()) {
					allQuests.add(quest);
					questMap.put(quest.getId(), Map.of(group, club));
				}
			}
		}

		// 4. 퀘스트 없으면 예외
		if (allQuests.isEmpty()) {
			throw new CustomException(ErrorCode.QUEST_NOT_FOUND);
		}

		// 5. Quest -> CurQuestTotalModel 변환
		List<CurQuestTotalModel> curQuestTotalModels = allQuests.stream()
				.map(quest -> CurQuestTotalModel.of(quest, questMap.get(quest.getId())))
				.toList();

		return QuestModel.builder()
				.id(email)
				.curQuestTotalList(curQuestTotalModels)
				.build();
	}
}
