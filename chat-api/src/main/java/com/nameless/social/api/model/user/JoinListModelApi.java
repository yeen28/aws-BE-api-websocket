package com.nameless.social.api.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinListModelApi {
	private String groupname;
	private String[] clubList;
}
