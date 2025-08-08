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

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
	IMAGE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "U002", "User Image don't update"),

	// Group
	GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "GR001", "Group not found"),

	// Club
	CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "CR001", "Club not found"),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
