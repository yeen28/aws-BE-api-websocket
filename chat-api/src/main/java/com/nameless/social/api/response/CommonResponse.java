package com.nameless.social.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nameless.social.api.exception.ErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
	private final String result;
	private final T data;
	private final String message;
	private final ErrorResponse error;

	private CommonResponse(T data) {
		this.result = "SUCCESS";
		this.data = data;
		this.message = "SUCCESS";
		this.error = null;
	}

	private CommonResponse(ErrorResponse error) {
		this.result = "FAIL";
		this.data = null;
		this.message = error.getMessage();
		this.error = error;
	}

	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<>(data);
	}

	public static CommonResponse<Void> success() {
		return new CommonResponse<>(null);
	}

	public static CommonResponse<ErrorResponse> error(ErrorResponse error) {
		log.warn(">>> CommonResponse Error: {}", error.getMessage());
		return new CommonResponse<>(error);
	}
}
