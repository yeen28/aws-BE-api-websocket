package com.nameless.social.api.service;

import com.nameless.social.api.dto.ChatRoomDto;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.chat.ChatRoomModel;
import com.nameless.social.api.repository.chat.ChatRoomRepository;
import com.nameless.social.api.repository.chat.ChatRoomUserRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.ChatRoomUser;
import com.nameless.social.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;

	@Transactional
	public ChatRoomModel createChatRoom(final ChatRoomDto chatRoomDto) {
		ChatRoom chatRoom = new ChatRoom(chatRoomDto.getName());
		chatRoomRepository.save(chatRoom);

		List<User> participants = userRepository.findAllById(chatRoomDto.getParticipantIds());
		for (User user : participants) {
			ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoom);
			chatRoomUserRepository.save(chatRoomUser);
		}

		return ChatRoomModel.of(chatRoom);
	}

	public ChatRoomModel findById(final Long id) {
		return ChatRoomModel.of(chatRoomRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND)));
	}

	public List<ChatRoomModel> findAll() {
		return chatRoomRepository.findAll().stream()
				.map(ChatRoomModel::of)
				.collect(Collectors.toList());
	}
}
