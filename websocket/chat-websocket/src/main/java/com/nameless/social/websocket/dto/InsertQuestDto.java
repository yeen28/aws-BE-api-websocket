package com.nameless.social.websocket.dto;

public record InsertQuestDto(
	String title, // Quest title
	String description,
	long difficulty
) {}
