package com.nameless.social.api.service;

import com.nameless.social.api.dto.QuestSuccessDto;
import com.nameless.social.api.dto.UserQuestDto;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.*;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

		// 4. Quest -> CurQuestTotalModel 변환
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

	/**
	 * 현재 일자에 할당받은 퀘스트를 제외한 나머지 퀘스트 수행 여부를 집산한 것입니다.
	 * User, Quest 매핑 테이블을 통해 나오는 isSuccess 그리고 Quest 테이블로 들어가 나오는 group은 group 그대로, compleTime은 createTime을 넣어주시면 될 것같습니다
	 * @param email
	 * @return
	 */
	public UserQuestPrevModel getUserQuestPrev(final String email) {
		// 1. 유저 조회
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 2. 오늘 날짜
		LocalDate today = LocalDate.now();

		// 3. 유저가 속한 그룹 조회
		List<Group> groups = userGroupRepository.findAllByIdUserId(user.getId())
				.stream()
				.map(UserGroup::getGroup)
				.toList();

		// 4. 그룹에 속한 모든 클럽의 퀘스트 수집 (오늘 날짜 제외)
		List<QuestPrevTotalModel> prevQuests = new ArrayList<>();
		for (Group group : groups) {
			for (Club club : group.getClubs()) {
				for (Quest quest : club.getQuest()) {
					LocalDate createDate = quest.getCreatedAt().toLocalDate();

					// 오늘 날짜 제외
					if (!createDate.isEqual(today)) {
						prevQuests.add(
								QuestPrevTotalModel.builder()
										.questId(quest.getId())
										.quest(quest.getName())
										.group(group.getName())
										.isSuccess(quest.isSuccess())
										.completeTime(createDate) // createTime → LocalDate
										.build()
						);
					}
				}
			}
		}

		// 5. 결과 반환
		return UserQuestPrevModel.builder()
				.id(user.getEmail())
				.prevQuestTotalList(prevQuests)
				.build();
	}

	/**
	 * 사용자가 퀘스트 수행 여부를 최신화합니다.
	 * 사용자가 퀘스트 수행 완료를 누르면 해당 퀘스트 목록이 전송됩니다.
	 * @param dto
	 */
	public void setUserQuestRecord(final User user, final UserQuestDto dto) {
		log.info("아직 구현 중...");
	}

	/**
	 * 그룹에서 퀘스트를 성공한 인원이 나온 경우 알려주는 api입니다
	 * @param dto
	 */
	public void questSuccess(final User user, final QuestSuccessDto dto) {
		log.info("아직 구현 중...");
	}
}