package com.nameless.social.api.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurQuestTotalModelApi {
	private String quest;
	private String group;
	private boolean isSuccess;
}
