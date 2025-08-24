package com.nameless.social.core.model;

import com.nameless.social.core.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoModel {
    private long id;
	private String email;
	private String name;

	public static UserInfoModel of(User user) {
		return UserInfoModel.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.build();
	}
}
