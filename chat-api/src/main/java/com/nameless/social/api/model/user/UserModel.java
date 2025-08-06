package com.nameless.social.api.model.user;

import com.nameless.social.core.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserModel {
	private String id; // email
	private String name;
	private String avatar; // base64 encode + json stringify
	private Status status; // 확장 기능용. 현재는 그냥 “online” string으로 주세요
	private LocalDateTime joinDate;
	private LocalDateTime lastSeen;

	public enum Status {
		ONLINE, OFFLINE
	}

	public static UserModel of(User user) {
		return UserModel.builder()
				.id(user.getEmail())
				.name(user.getName())
				.avatar("")
				.status(Status.ONLINE)
				.joinDate(user.getCreatedAt())
				.lastSeen(user.getUpdatedAt())
				.build();
	}
}
