package com.nameless.social.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid Input Value"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "Method Not Allowed"),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "Entity Not Found"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "Server Error"),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "Invalid Type Value"),
	JSON_PROCESSING_EXCEPTION(HttpStatus.BAD_REQUEST, "C006", "Json processing exception"),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "C007", "invalid token"),

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
	IMAGE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "U002", "User Image don't update"),

	// Group
	GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GR001", "Group not found"),

	// Club
	CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "CR001", "Club not found"),

	// Quest
	QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "QU001", "Quest not found"),

	// UserGroup
	USER_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "UG001", "User is not a member of any group"),

	// UserClub
	USER_CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "UC001", "User is not a member of any club"),

	// UserQuest
	USER_QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "UQ001", "User Quest not found"),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
