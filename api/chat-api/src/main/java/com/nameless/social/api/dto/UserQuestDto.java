package com.nameless.social.api.dto;

import java.util.List;

public record UserQuestDto(
		String user,
		String group, // group name
		List<String> userQuest // ??? quest name
) {}
