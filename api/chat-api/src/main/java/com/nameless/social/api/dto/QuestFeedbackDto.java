package com.nameless.social.api.dto;

import java.util.List;

public record QuestFeedbackDto(
		String club, // club name
		String quest, // quest name
		List<String> feedback
) {}
