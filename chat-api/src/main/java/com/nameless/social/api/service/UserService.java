package com.nameless.social.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.api.dto.*;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.GenerateQuestModel;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.repository.*;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.model.UserInfoModel;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final QuestRepository questRepository;
	private final UserQuestRepository userQuestRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserClubRepository userClubRepository;
	private final ClubRepository clubRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${services.ai.url}")
	private String aiUrl;

	@Transactional
	public UserModel getOrCreateUser(final UserDto userDto) {
		Optional<User> userOptional = userRepository.findByToken(userDto.getToken());
		return UserModel.of(
				userOptional.orElseGet(() ->
						userRepository.save(new User(userDto.getToken(), userDto.getName(), userDto.getEmail()))
				)
		);
	}

	public UserModel findById(final Long id) {
		return UserModel.of(userRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}

	public List<UserModel> findAll() {
		return userRepository.findAll().stream()
				.map(UserModel::of).toList();
	}

	public void getUserQuestContinuous(final String email) {
	}

	public UserInfoModel findUserInfo(final String email) {
		return UserInfoModel.of(userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}

	public UserModel getUserInfo(final String email) {
		return UserModel.of(userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}

	@Transactional
	public void updateUsername(final User user, final UsernameDto dto) {
		userRepository.updateUsername(user.getId(), dto.getUsername());
	}

	@Transactional
	public void joinClub(final User user, final JoinClubDto dto) {
		// TODO group에 존재하는 user인지 유효성 검증하면 더 좋긴 함.
//		Group group = groupRepository.findByName(dto.getGroup())
//				.orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
		List<Club> clubs = clubRepository.findByNameList(dto.getClubList());
		if (clubs.isEmpty()) {
			log.warn("club is empty - {} {}", user.getId(), user.getEmail());
			throw new CustomException(ErrorCode.CLUB_NOT_FOUND);
		}

		List<UserClub> userClubs = new ArrayList<>();
		for (Club club : clubs) {
			userClubs.add(new UserClub(user, club));
		}
		userClubRepository.saveAll(userClubs);

		addUserQuest(user, clubs);
	}

	@Transactional
	public void joinGroup(final User user, final JoinGroupDto dto) {
		// TODO group에 존재하는 user인지 유효성 검증하면 더 좋긴 함.
		Group group = groupRepository.findByName(dto.getGroup())
				.orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
		if (group == null) {
			log.warn("group is empty - {} {}", user.getId(), user.getEmail());
			throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
		}

		UserGroup userGroup = new UserGroup(user, group);
		userGroupRepository.save(userGroup);
	}

	// TODO 좀 더 살펴보기
	@Transactional
	public void leaveGroup(final User user, final LeaveGroupDto dto) {
		if (dto.getGroup() == null) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
		}

		Group group = groupRepository.findByName(dto.getGroup())
				.orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

		UserGroupId userGroupId = new UserGroupId(user.getId(), group.getId());
		UserGroup userGroup = userGroupRepository.findById(userGroupId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_GROUP_NOT_FOUND));

		List<Club> groupClubs = group.getClubs();
		List<Long> clubIds = groupClubs.stream()
				.map(Club::getId)
				.collect(Collectors.toList());

		List<UserClub> userClubs = userClubRepository.findByUserClubsInClubIds(user.getId(), clubIds);

		userClubRepository.deleteAll(userClubs);  // 먼저 Club 탈퇴

		// 마지막으로 그룹 탈퇴 (UserGroup 삭제)
		userGroupRepository.delete(userGroup);
	}

	@Transactional
	public void leaveClub(final User user, final LeaveClubDto leaveClubDto) {
		Club club = clubRepository.findByName(leaveClubDto.getClub())
				.orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

		// TODO 퀘스트에 delete flag 하나 있어야 함 -> 이유: 통계를 위해 컬럼은 있어야 하기 때문. 재가입을 해도 이전 퀘스트가 보임.
		userClubRepository.deleteByIdUserIdAndClubId(user.getId(), club.getId());
	}

	@Transactional
	public void deleteUser(final String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		final long deleteUserId = user.getId();

		// existsByIdUserId : DB에서 존재 여부 확인 시 LIMIT 1 쿼리로 처리 → 훨씬 빠름
		if (!userClubRepository.existsByIdUserId(deleteUserId)) {
			throw new CustomException(ErrorCode.USER_CLUB_NOT_FOUND);
		}

		if (!userGroupRepository.existsByIdUserId(deleteUserId)) {
			throw new CustomException(ErrorCode.USER_GROUP_NOT_FOUND);
		}

		userClubRepository.deleteByIdUserId(deleteUserId);
		userGroupRepository.deleteByIdUserId(deleteUserId);
		userRepository.deleteById(deleteUserId);
	}

	/**
	 * 사용자가 club에 가입하면 club에 해당하는 퀘스트들을 사용자에게 할당합니다.
	 * 하지만 해당 club에서 이미 오늘 퀘스트를 받았던 사용자(오늘 탈퇴 후 바로 재가입)라면 퀘스트는 추가로 할당되지 않습니다.
	 * @param user
	 * @param clubs
	 */
	private void addUserQuest(final User user, final List<Club> clubs) {
		log.info("add user quest >>> {}", user.getEmail());
		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);

		clubs.forEach(club -> {
			List<Quest> questsToday = questRepository.findByCreatedAtBetween(start, end).stream()
					.filter(quest -> quest.getClub().getId() == club.getId())
					.toList();

			if (questsToday.isEmpty()) {
				GenerateQuestModel generateQuestModel = null;
				try {
					GenerateQuestDto generateQuestDto = new GenerateQuestDto(user.getEmail(), String.valueOf(club.getId()), club.getName());
					generateQuestModel = restTemplate.postForObject(
							String.format("%s/generateQuest", aiUrl),
							generateQuestDto,
							GenerateQuestModel.class
					);
					log.info("Successfully called AI API. Response for user {}: {}", generateQuestModel.getUser(), objectMapper.writeValueAsString(generateQuestModel.getQuestList()));
				} catch (Exception e) {
					log.error("Error calling API: {} {} {}", club.getId(), user.getEmail(), e.getMessage());
				}

				List<InsertQuestDto> insertQuestDtoList = new ArrayList<>();
				generateQuestModel.getQuestList().forEach(questModel -> {
					insertQuestDtoList.add(new InsertQuestDto(
							questModel.getQuestTitle(),
							questModel.getQuestDescription(),
							questModel.getDifficulty()
					));
				});

				insertQuestDtoList.forEach(dto -> {
					Quest quest = Quest.builder()
							.name(dto.title())
							.description(dto.description())
							.club(club)
							.difficulty(dto.difficulty())
							.build();
					questRepository.save(quest);
				});

				questsToday = questRepository.findByCreatedAtBetween(start, end).stream()
						.filter(quest -> quest.getClub().getId() == club.getId())
						.toList();
			}

			List<Quest> clubQuestsToday = questsToday.stream()
					.filter(quest -> quest.getClub().getId() == club.getId())
					.toList();

			List<UserQuest> userQuestsToSave = new ArrayList<>();

			for (Quest quest : clubQuestsToday) {
				UserQuestId userQuestId = new UserQuestId(user.getId(), quest.getId());
				Optional<UserQuest> existingUserQuestOpt = userQuestRepository.findById(userQuestId);

				if (existingUserQuestOpt.isPresent()) {
					UserQuest existingUserQuest = existingUserQuestOpt.get();
					if (!existingUserQuest.isVisible()) {
						existingUserQuest.setVisible(true);
					}
				} else {
					userQuestsToSave.add(new UserQuest(user, quest));
				}
			}

			if (!userQuestsToSave.isEmpty()) {
				userQuestRepository.saveAll(userQuestsToSave);
			}
		});
	}
}
