package com.nameless.social.api.service;

import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.ClubUser;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	public List<GroupModel> getGroupListByUserEmail(final String email) {
		User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		List<ClubUser> chatRooms = user.getClubs();
		return chatRooms.stream()
				.map(chatRoomUser -> GroupModel.of(email, chatRoomUser)).toList();
	}

	public GroupInfoModel getGroupInfo(String groupName) {
		Group group = groupRepository.findByName(groupName)
				.orElseThrow(EntityNotFoundException::new);
		return GroupInfoModel.of(group);
	}

	public List<GroupInfoModel> getGroupList() {
		return groupRepository.findAll().stream()
						.map(GroupInfoModel::of)
						.toList();
	}
}
