package com.nameless.social.api.model.chat;

import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.ChatRoomUser;
import com.nameless.social.api.model.user.UserModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ChatRoomModel {
	private Long id;
	private String name;
	private List<UserModel> participants;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ChatRoomModel of(ChatRoom chatRoom) {
		return ChatRoomModel.builder()
				.id(chatRoom.getId())
				.name(chatRoom.getName())
				.participants(chatRoom.getParticipants().stream().map(ChatRoomModel::toUserModel).collect(Collectors.toList()))
				.createdAt(chatRoom.getCreatedAt())
				.updatedAt(chatRoom.getUpdatedAt())
				.build();
	}

	private static UserModel toUserModel(ChatRoomUser chatRoomUser) {
		return UserModel.builder()
				.id(chatRoomUser.getUser().getEmail())
				.name(chatRoomUser.getUser().getName())
				.status(UserModel.Status.ONLINE)
				.joinDate(chatRoomUser.getUser().getCreatedAt())
				.lastSeen(chatRoomUser.getUser().getUpdatedAt())
				.build();
	}
}
