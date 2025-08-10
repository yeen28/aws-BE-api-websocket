package com.nameless.social.api.dto;

import java.util.List;

public record QuestSuccessDto(
		String user, // email
		String group, // group name
		List<QuestFeedbackDto> questList
) {}
