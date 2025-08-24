package com.nameless.social.api.controller;

import com.nameless.social.api.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
	@Operation(summary = "모든 사용자 정보 조회")
	@GetMapping
	public CommonResponse<Object> getUsers() {
		// TODO 모든 사용자 조회
		return CommonResponse.success(HttpStatus.OK);
	}

	@Operation(summary = "단일 사용자 정보 조회")
	@GetMapping("/{id}")
	public CommonResponse<Object> getUser() {
		// TODO 단일 사용자 조회
		return CommonResponse.success(HttpStatus.OK);
	}
}
