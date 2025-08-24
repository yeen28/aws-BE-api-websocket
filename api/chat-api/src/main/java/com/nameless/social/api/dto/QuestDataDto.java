package com.nameless.social.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestDataDto {
	private String date;
	private String clubId;
	private String clubTitle;
	private String questTitle;
	private String questDescription;
	@JsonProperty("questDifficulty") // JSON field name is different from Java field name
	private long difficulty;
}
