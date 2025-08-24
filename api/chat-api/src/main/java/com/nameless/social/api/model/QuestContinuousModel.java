package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestContinuousModel {
	private String id; // user email
	private List<QuestContinuousSuccessModel> continuousSuccessQuestList;
}
