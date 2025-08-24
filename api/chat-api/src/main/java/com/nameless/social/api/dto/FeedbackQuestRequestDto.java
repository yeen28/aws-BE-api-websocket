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
public class FeedbackQuestRequestDto {
	private String user;
	private String questTitle;
	private String clubId;
	private String clubTitle;
	private String feedback;
	@JsonProperty("isLike")
	private boolean isLike;
}
