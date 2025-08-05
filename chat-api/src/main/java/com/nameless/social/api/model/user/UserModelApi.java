package com.nameless.social.api.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserModelApi {
	private String id; // email 값
	private List<JoinListModelApi> joinList;
}
