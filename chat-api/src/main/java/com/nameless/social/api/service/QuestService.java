package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.CurQuestTotalModel;
import com.nameless.social.api.model.QuestModel;
import com.nameless.social.api.model.QuestWeeklyModel;
import com.nameless.social.api.model.UserQuestWeeklyModel;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

	/**
	 * 사용자가 성공한 퀘스트를 요일별로 집산하고 각 요일별로 가장 활발하게 퀘스트를 클리어한 그룹을 명시합니다.
	 * 요일별로 배열 구성하고 createTime을 Date 함수를 통해 일자로 변환하여 요일별 배열에 카운팅하면 될 것 같습니다.
	 * @param email
	 * @return
	 */
	public UserQuestWeeklyModel getUserQuestWeekly(final String email) {
		// 1. 유저 조회
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 2. 유저가 속한 그룹과 클럽 → 퀘스트 목록 수집
		List<Group> groups = userGroupRepository.findAllByIdUserId(user.getId())
				.stream()
				.map(UserGroup::getGroup)
				.toList();

		List<Quest> allQuests = new ArrayList<>();
		Map<Long, Map<Group, Club>> questMap = new HashMap<>(); // questId → {Group, Club} 매핑

		for (Group group : groups) {
			for (Club club : group.getClubs()) {
				for (Quest quest : club.getQuest()) {
					allQuests.add(quest);
					questMap.put(quest.getId(), Map.of(group, club));
				}
			}
		}

		if (allQuests.isEmpty()) {
			throw new CustomException(ErrorCode.QUEST_NOT_FOUND);
		}

		// 3. 요일별 집계 구조 생성
		Map<Integer, List<Quest>> questsByDay = allQuests.stream()
				.collect(Collectors.groupingBy(
						quest -> {
							LocalDateTime created = quest.getCreatedAt();
							return created.getDayOfWeek().getValue() % 7;  // 일요일=0, 월=1 ... 토=6
						}
				));

		// 4. QuestWeeklyModel 생성
		List<QuestWeeklyModel> weeklyList = new ArrayList<>();

		for (int day = 0; day < 7; day++) {
			List<Quest> questsInDay = questsByDay.getOrDefault(day, List.of());

			long totalNum = questsInDay.size();
			long successNum = questsInDay.stream().filter(Quest::isSuccess).count();

			// 그룹별 성공 퀘스트 수 집계
			String bestGroup = questsInDay.stream()
					.filter(Quest::isSuccess)
					.collect(Collectors.groupingBy(
							q -> questMap.get(q.getId()).keySet().iterator().next().getName(),
							Collectors.counting()
					))
					.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey)
					.orElse(null);

			weeklyList.add(
					QuestWeeklyModel.builder()
							.day(day)
							.questTotalNum(totalNum)
							.successQuestNum(successNum)
							.bestParticipateGroup(bestGroup)
							.build()
			);
		}

		return UserQuestWeeklyModel.builder()
				.id(user.getEmail())
				.weeklyQuestList(weeklyList)
				.build();
	}
}
