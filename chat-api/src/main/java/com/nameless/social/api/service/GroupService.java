package com.nameless.social.api.service;

import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.chat.ChatRoomRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.ChatRoomUser;
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
	private final ChatRoomRepository groupRepository;

	public List<GroupModel> getGroupListByUserEmail(final String email) {
		User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		List<ChatRoomUser> chatRooms = user.getChatRooms();
		return chatRooms.stream()
				.map(chatRoomUser -> GroupModel.of(email, chatRoomUser)).toList();
	}

	public GroupInfoModel getGroupInfo(String groupName) {
		ChatRoom group = groupRepository.findByName(groupName)
				.orElseThrow(EntityNotFoundException::new);
		return GroupInfoModel.of(group);
	}
}
