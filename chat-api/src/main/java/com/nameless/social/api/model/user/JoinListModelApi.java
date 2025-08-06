package com.nameless.social.api.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JoinListModelApi {
	private String groupname;
	private List<String> clubList;
}
