package com.nameless.social.api.exception;

import com.nameless.social.api.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<CommonResponse<ErrorResponse>> handleCustomException(CustomException e) {
		final ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
		return new ResponseEntity<>(CommonResponse.error(errorResponse), e.getErrorCode().getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<CommonResponse<ErrorResponse>> handleException(Exception e) {
		final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(CommonResponse.error(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
