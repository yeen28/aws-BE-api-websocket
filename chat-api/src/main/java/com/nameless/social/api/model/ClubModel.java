package com.nameless.social.api.model;

import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.ClubUser;
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
public class ClubModel {
	private Long id;
	private String name;
	private List<UserModel> participants;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ClubModel of(Club club) {
		return ClubModel.builder()
				.id(club.getId())
				.name(club.getName())
				.participants(club.getParticipants().stream().map(ClubModel::toUserModel).collect(Collectors.toList()))
				.createdAt(club.getCreatedAt())
				.updatedAt(club.getUpdatedAt())
				.build();
	}

	private static UserModel toUserModel(ClubUser clubUser) {
		return UserModel.builder()
				.id(clubUser.getUser().getEmail())
				.name(clubUser.getUser().getName())
				.status(UserModel.Status.ONLINE)
				.joinDate(clubUser.getUser().getCreatedAt())
				.lastSeen(clubUser.getUser().getUpdatedAt())
				.build();
	}
}
