package com.nameless.social.api.dto;

public record InsertQuestDto (
	String name,
	String description,
	String tags,
	String clubId // 어떤 club의 quest인지
) {}
