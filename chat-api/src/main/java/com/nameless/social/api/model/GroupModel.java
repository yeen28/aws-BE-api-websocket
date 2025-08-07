package com.nameless.social.api.model;

import com.nameless.social.api.model.user.JoinListModelApi;
import com.nameless.social.core.entity.UserClub;
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
	private List<JoinListModelApi> joinList;

	public static GroupModel of(String email, UserClub clubUser) {
		return GroupModel.builder()
				.id(email)
				.joinList(new ArrayList<>()) // TODO joinList를 실제값으로 채우기
				.build();
	}
}
