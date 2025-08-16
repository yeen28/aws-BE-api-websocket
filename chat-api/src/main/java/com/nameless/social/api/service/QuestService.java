package com.nameless.social.api.service;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.*;
import com.nameless.social.api.repository.ClubRepository;
import com.nameless.social.api.repository.QuestRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QuestService {
	private final ClubRepository clubRepository;
	private final UserRepository userRepository;
	private final UserGroupRepository userGroupRepository;
	private final QuestRepository questRepository;
	private final RestTemplate restTemplate;

	@Value("${services.ai.url}")
	private String aiUrl;

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
	 * 퀘스트 성공 처리
	 * 그룹에서 퀘스트를 성공한 인원이 나온 경우 알려주는 api입니다
	 * @param dto
	 */
	@Transactional
	public void questSuccess(final User user, final QuestSuccessDto dto) {
		// TODO dto.quest()로 조회해야하는데 FE에서 잘못 전달하고 있음. 지금은 급하니깐 일단 이렇게 하기
		Quest quest = questRepository.findByName(dto.club())
				.orElseThrow(() -> new CustomException(ErrorCode.QUEST_NOT_FOUND));

		// 본인 소유 퀘스트인지 검증
		if (quest.getUser().getId() != user.getId()) {
			log.warn("다른 사용자의 퀘스트를 조작할 수 없습니다. - userId:{}, questId:{}", user.getId(), quest.getId());
			throw new IllegalStateException("다른 사용자의 퀘스트를 수정할 수 없습니다.");
		}

		quest.setSuccess(true); // 변경 감지로 update 실행

		sendFeedbackToAI(user, quest, dto);
	}

	/**
	 * 사용자가 퀘스트를 몇일 연속으로 클리어했는지 표시합니다.
	 * 반환하는 데이터는 30일 기준입니다.
	 * @param user
	 * @return
	 */
	public QuestContinuousModel getQuestStatisticsByUser(final User user) {
		LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
		LocalDate yesterday = LocalDate.now().minusDays(1);

		// 30일 이내의 퀘스트만 필터링
		List<Quest> recentQuests = user.getQuests().stream()
				.filter(q -> q.getCreatedAt().isAfter(thirtyDaysAgo))
				.toList();

		long totalQuestNum = recentQuests.size();
		long successQuestNum = recentQuests.stream()
				.filter(Quest::isSuccess)
				.count();

		// 연속 성공 일수 계산
		long continuousDays = 0;
		LocalDate currentDate = yesterday;

		while (continuousDays <= 30) {
			LocalDate finalCurrentDate = currentDate;
			boolean hasSuccessOnDay = recentQuests.stream()
					.anyMatch(q -> q.isSuccess() && q.getCreatedAt().toLocalDate().isEqual(finalCurrentDate));

			if (hasSuccessOnDay) {
				continuousDays++;
				currentDate = currentDate.minusDays(1);
			} else {
				break;
			}
		}

		QuestContinuousSuccessModel successModel = QuestContinuousSuccessModel.builder()
				.days(continuousDays)
				.totalQuestNum(totalQuestNum)
				.successQuestNum(successQuestNum)
				.build();

		return QuestContinuousModel.builder()
				.id(user.getEmail())
				.continuousSuccessQuestList(Collections.singletonList(successModel))
				.build();
	}

	public void insertQuest(final long clubId, final List<InsertQuestDto> dtoList) {
		Club club = clubRepository.findById(clubId)
				.orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

		dtoList.forEach(dto -> {
			Quest quest = Quest.builder()
					.name(dto.title())
					.description(dto.description())
					.isSuccess(false)
					.club(club)
					.difficulty(dto.difficulty())
					.build();
			questRepository.save(quest);
		});
	}

	private void sendFeedbackToAI(User user, Quest quest, QuestSuccessDto dto) {
		final String url = String.format("%s/feedbackQuest", aiUrl);

		try {
			String feedback = dto.feedback();
			FeedbackQuestRequestDto requestDto = new FeedbackQuestRequestDto(
					user.getEmail(),
					quest.getName(),
					String.valueOf(quest.getClub().getId()),
					quest.getClub().getName(),
					feedback,
					dto.isLike()
			);

			String response = restTemplate.postForObject(url, requestDto, String.class);
			log.info("Successfully called AI feedback API. Response: {}", response);

		} catch (Exception e) {
			log.error("Error calling AI feedback API for user: {}", user.getEmail(), e);
		}
	}

	/**
	 * 오늘 퀘스트가 할당되었는지 체크
	 * @param clubId
	 * @return
	 */
	public boolean existQuest(final long clubId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
		return questRepository.countByClubIdAndCreatedAtBetween(clubId, startOfDay, endOfDay) >= 3;
	}
}