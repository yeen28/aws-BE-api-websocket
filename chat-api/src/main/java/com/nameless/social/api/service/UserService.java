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
import com.nameless.social.core.entity.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

	public UserModel getUserInfo(final String email) {
		// TODO User 유효성 검증
		return UserModel.of(userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}

	@Transactional
	public void updateUsername(final UsernameDto dto) {
		if (StringUtils.isEmpty(dto.getUser())) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
		}

		User user = userRepository.findByEmail(dto.getUser())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		userRepository.updateUsername(user.getId(), dto.getUsername());
	}

	@Transactional
	public void joinClub(final JoinClubDto dto) {
		if (StringUtils.isEmpty(dto.getUser())) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
		}

		User user = userRepository.findByEmail(dto.getUser())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
	public void joinGroup(final JoinGroupDto dto) {
		if (StringUtils.isEmpty(dto.getUser())) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
		}

		User user = userRepository.findByEmail(dto.getUser())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
	public void leaveGroup(final LeaveGroupDto dto) {
		if (dto.getUser() == null || dto.getGroup() == null) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
		}

		User user = userRepository.findByEmail(dto.getUser())
				.orElseThrow(EntityNotFoundException::new);
		Group group = groupRepository.findByName(dto.getGroup())
				.orElseThrow(EntityNotFoundException::new);

		UserGroupId userGroupId = new UserGroupId(user.getId(), group.getId());
		// TODO 임시로 1L로 조회
//		UserGroup userGroup = userGroupRepository.findById(userGroupId)
		UserGroup userGroup = userGroupRepository.findById(1L)
				.orElseThrow(EntityNotFoundException::new);

		List<Club> groupClubs = group.getClubs();
		List<Long> clubIds = groupClubs.stream()
				.map(Club::getId)
				.collect(Collectors.toList());

		List<UserClub> userClubs = userClubRepository.findUserClubsInClubIds(user.getId(), clubIds);

		userClubRepository.deleteAll(userClubs);  // 먼저 Club 탈퇴

		// 마지막으로 그룹 탈퇴 (UserGroup 삭제)
		userGroupRepository.delete(userGroup);
	}

	@Transactional
	public void leaveClub(final LeaveClubDto leaveClubDto) {
		User user = userRepository.findByEmail(leaveClubDto.getUser())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Club club = clubRepository.findByName(leaveClubDto.getClub())
				.orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

		userClubRepository.deleteByUserIdAndClubId(user.getId(), club.getId());
	}
}
