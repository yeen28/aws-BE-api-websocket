package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.UserClub;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	public List<GroupModel> getGroupListByUserEmail(final String email) {
		User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		List<UserClub> userClubs = user.getUserClubs();
		return userClubs.stream()
				.map(userClub -> GroupModel.of(email, userClub)).toList();
	}

	public GroupInfoModel getGroupInfo(String groupName) {
		Group group = groupRepository.findByName(groupName)
				.orElseThrow(EntityNotFoundException::new);
		return GroupInfoModel.of(group);
	}

	public List<GroupInfoModel> getGroupList() {
		List<GroupInfoModel> groupList = groupRepository.findAll().stream()
				.map(GroupInfoModel::of)
				.toList();

		if (groupList.isEmpty()) {
			log.warn("GroupList is null.");
			throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
		}

		return groupList;
	}
}
