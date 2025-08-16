package com.nameless.social.api.service;

import com.nameless.social.api.dto.*;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.repository.ClubRepository;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.UserClubRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.model.UserInfoModel;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserClubRepository userClubRepository;
	private final ClubRepository clubRepository;

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
}
