package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomModel {
	private Long id;
	private String name;
	private List<UserModel> participants;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
