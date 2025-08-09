package com.nameless.social.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.UserClub;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.User;
import com.nameless.social.core.entity.UserGroup;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public GroupModel getGroupByUserEmail(final String email) {
		List<String> groupNames = new ArrayList<>();
		List<String> clubNames = new ArrayList<>();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		List<UserGroup> userGroups = user.getUserGroups();
		for (UserGroup userGroup : userGroups) {
			String groupName = userGroup.getGroup().getName();
			groupNames.add(groupName);
		}

		List<UserClub> userClubs = user.getUserClubs();
		for (UserClub userClub : userClubs) {
			String clubName = userClub.getClub().getName();
			clubNames.add(clubName);
		}

		return GroupModel.of(user.getEmail(), groupNames, clubNames);
	}

	public GroupInfoModel getGroupInfo(String groupName) {
		Group group = groupRepository.findByName(groupName)
				.orElseThrow(() -> new EntityNotFoundException("Group Not Found"));
		return GroupInfoModel.of(group, parseTags(group.getTag()));
	}

	public List<GroupInfoModel> getGroupList() {
		List<GroupInfoModel> groupList = groupRepository.findAll().stream()
				.map(group -> GroupInfoModel.of(group, parseTags(group.getTag())))
				.toList();

		if (groupList.isEmpty()) {
			log.warn("GroupList is null.");
			throw new CustomException(ErrorCode.ENTITY_NOT_FOUND);
		}

		return groupList;
	}

	private List<String> parseTags(String stringTags) {
		try {
			return OBJECT_MAPPER.readValue(stringTags, new TypeReference<List<String>>() {});
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_PROCESSING_EXCEPTION);
		}
	}
}
