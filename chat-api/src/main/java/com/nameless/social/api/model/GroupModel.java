package com.nameless.social.api.model;

import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class GroupModel {
	private String id; // email 값
	private List<JoinListModel> joinList;

	public static GroupModel of(List<Group> groups, List<Club> clubs, String email) {
		List<JoinListModel> joinListModels = new ArrayList<>();
		for (Group group : groups) {
			List<ClubModel> clubModels = new ArrayList<>();
			for (Club club : clubs) {
				if (club.getGroup().getId() == group.getId()) {
					clubModels.add(ClubModel.builder()
							.clubId(club.getId())
							.name(club.getName())
							.createdAt(club.getCreatedAt())
							.updatedAt(club.getUpdatedAt())
							.build()
					);
					joinListModels.add(JoinListModel.builder()
							.groupId(group.getId())
							.groupname(group.getName())
							.clubList(clubModels)
							.build());
				}
			}
		}

		return GroupModel.builder()
				.id(email)
				.joinList(joinListModels)
				.build();
	}
}
