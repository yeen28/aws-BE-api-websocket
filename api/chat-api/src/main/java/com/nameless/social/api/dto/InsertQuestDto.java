package com.nameless.social.api.dto;

public record InsertQuestDto(
	String title, // Quest title
	String description,
	long difficulty
) {}
