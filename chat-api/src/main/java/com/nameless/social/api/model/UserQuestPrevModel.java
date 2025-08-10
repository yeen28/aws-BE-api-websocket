package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserQuestPrevModel {
	private String id; // email 값
	private List<QuestPrevTotalModel> prevQuestTotalList;
}
