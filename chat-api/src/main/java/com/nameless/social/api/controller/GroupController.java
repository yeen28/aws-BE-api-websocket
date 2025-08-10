package com.nameless.social.api.controller;

import com.nameless.social.api.handler.UserInfo;
import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.GroupService;
import com.nameless.social.core.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {
	private final GroupService groupService;

	@Operation(summary = "사용자가 가입한 그룹 조회")
	@GetMapping("/user/getUserJoin")
	public CommonResponse<GroupModel> getGroupListByUserEmail(
			@UserInfo final User user,
			@RequestParam(value = "email", required = false) final String email
	) {
		return CommonResponse.success(groupService.getGroupByUserEmail(user.getEmail()));
	}

	@Operation(summary = "그룹 정보 조회")
	@GetMapping("/group/getGroupInfo")
	public CommonResponse<GroupInfoModel> getGroupInfo(
			@UserInfo final User user,
			@RequestParam(value = "name", required = false) final String groupName
	) {
		return CommonResponse.success(groupService.getGroupInfo(groupName));
	}

	@Operation(summary = "전체 그룹 목록")
	@GetMapping("/group/getGroupList")
	public CommonResponse<List<GroupInfoModel>> getGroupList(@UserInfo final User user) {
		return CommonResponse.success(groupService.getGroupList());
	}
}
