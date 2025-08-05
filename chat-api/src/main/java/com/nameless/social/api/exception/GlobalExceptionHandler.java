package com.nameless.social.api.exception;

import com.nameless.social.api.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<CommonResponse<ErrorResponse>> handleCustomException(CustomException e) {
		log.warn(">>> Custom Exception: {}", e.getMessage());
		final ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
		return new ResponseEntity<>(CommonResponse.error(errorResponse), e.getErrorCode().getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<CommonResponse<ErrorResponse>> handleException(Exception e) {
		log.warn(">>> Exception: {}", e.getMessage());
		final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(CommonResponse.error(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
