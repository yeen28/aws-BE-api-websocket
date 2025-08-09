package com.nameless.social.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.model.QuestModel;
import com.nameless.social.api.repository.ClubRepository;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
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
	private final ClubRepository clubRepository;
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
				.orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
		List<Club> clubs = clubRepository.findAllByGroupId(group.getId());

		// TODO mock data -> DB에서 club에 가입한 사용자 수를 조회해야 함.
		long memberCount = 1L;

		// TODO Quest를 DB에서 조회한 뒤 전달해야 함.
		List<QuestModel> questModels = null;

		return GroupInfoModel.of(
				group,
				clubs,
				memberCount,
				questModels,
				parseTags(group.getTag())
		);
	}

	public List<GroupInfoModel> getGroupList() {
		List<GroupInfoModel> groupList = groupRepository.findAll().stream()
				.map(group -> {
					List<Club> club = clubRepository.findAllByGroupId(group.getId());

					// TODO mock data -> DB에서 club에 가입한 사용자 수를 조회해야 함.
					long memberCount = 1L;

					// TODO Quest를 DB에서 조회한 뒤 전달해야 함.
					List<QuestModel> questModels = null;

					return GroupInfoModel.of(
							group,
							club,
							memberCount,
							questModels,
							parseTags(group.getTag())
					);
				})
				.toList();

		if (groupList.isEmpty()) {
			throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
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
