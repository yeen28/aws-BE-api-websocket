package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class GroupModel {
	private String id; // email 값 // TODO 이미 FE에서 가지고 있기 때문에 BE에서 또 전달할 필요가 없음
	private List<JoinListModel> joinList;

	public static GroupModel of(String email, List<String> groupNames, List<String> clubNames) {
		List<JoinListModel> joinListModels = new ArrayList<>();

		for (String groupName : groupNames) {
			joinListModels.add(JoinListModel.builder()
					.groupname(groupName)
					.clubList(clubNames)
					.build());
		}

		return GroupModel.builder()
				.id(email)
				.joinList(joinListModels)
				.build();
	}
}
