package com.nameless.social.api.controller;

import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {
	private final GroupService groupService;

	// TODO RESTful API로 수정
	// TODO 가능하면 TDD...
	@Operation(summary = "사용자가 가입한 그룹 목록 조회")
	@GetMapping("/user/getUserJoinList")
	public CommonResponse<List<GroupModel>> getGroupListByUserEmail(
			final HttpServletRequest request, // TODO ArgumentResolver로 Authorization 인증확인하도록 하기 -> final User user
			@RequestParam(value = "email", required = false) final String email
	) {
		// TODO Email을 클라이언트에서 가지고 있기 때문에 서버에서 또 이베일을 전달해줄 필요가 없음.
		// TODO 그리고 헤더에 인증 토큰을 보내기 때문에 email을 전달할 필요가 없음.
		return CommonResponse.success(groupService.getGroupListByUserEmail(email));
	}

	@GetMapping("/group/getGroup")
	public CommonResponse<Object> getGroup(
			final HttpServletRequest request,
			@RequestParam(value = "groupname", required = false) final String groupName
	) {
		return CommonResponse.success(groupService.getGroupInfo(groupName));
	}
}
