package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CurQuestTotalModel {
	private String quest;
	private String group;
	private boolean isSuccess;
}
