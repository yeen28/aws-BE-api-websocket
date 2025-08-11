package com.nameless.social.api.model;

import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.UserGroup;
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

	public static GroupModel of(List<UserGroup> userGroups, List<Club> clubs, String email) {
		List<JoinListModel> joinListModels = new ArrayList<>();
		for (UserGroup userGroup : userGroups) {
			List<ClubModel> clubModels = new ArrayList<>();
			for (Club club : clubs) {
				clubModels.add(ClubModel.builder()
						.clubId(club.getId())
						.name(club.getName())
						.build()
				);
			}

			joinListModels.add(JoinListModel.builder()
					.groupId(userGroup.getGroup().getId())
					.groupname(userGroup.getGroup().getName())
					.clubList(clubModels)
					.build());
		}

		return GroupModel.builder()
				.id(email)
				.joinList(joinListModels)
				.build();
	}
}
