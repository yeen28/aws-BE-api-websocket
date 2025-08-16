package com.nameless.social.api.dto;

public record QuestSuccessDto(
		String user, // email
		String group, // group name
		String club,
		String quest,
		String feedback,
		boolean isLike
) {}
