package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class QuestPrevTotalModel {
	private long questId;
	private String quest; // quest name
	private String group; // group name
	private boolean isSuccess;
	private LocalDate completeTime;
}
