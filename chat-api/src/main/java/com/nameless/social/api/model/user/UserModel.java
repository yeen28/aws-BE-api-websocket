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
	private Long id;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static UserModel of(User user) {
		return UserModel.builder()
				.id(user.getId())
				.username(user.getUsername())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}
