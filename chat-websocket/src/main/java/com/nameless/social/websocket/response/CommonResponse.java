package com.nameless.social.websocket.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nameless.social.websocket.exception.ErrorResponse;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
	private final String result;
	private final T data;
	private final String message;
	private final ErrorResponse error;

	@JsonCreator
	public CommonResponse(
			@JsonProperty("result") String result,
			@JsonProperty("data") T data,
			@JsonProperty("message") String message,
			@JsonProperty("error") ErrorResponse error
	) {
		this.result = result;
		this.data = data;
		this.message = message;
		this.error = error;
	}

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

	public boolean isSuccess() {
		return "SUCCESS".equals(result);
	}
}