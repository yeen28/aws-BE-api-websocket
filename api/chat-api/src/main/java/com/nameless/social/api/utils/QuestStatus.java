package com.nameless.social.api.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestStatus {
	SUCCESS(true, "SUCCESS"),
	FAIL(false, "FAIL");

	private boolean sig;
	private String message;
}
