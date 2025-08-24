package com.nameless.social.api.model;

import com.nameless.social.api.dto.QuestDataDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestModel {
	private String user;
	private List<QuestDataDto> questList;
}
