package com.nameless.social.api.controller;

import com.nameless.social.api.dto.ChatRoomDto;
import com.nameless.social.api.model.ChatRoomModel;
import com.nameless.social.api.response.CommonResponse;
import com.nameless.social.api.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	@PostMapping
	public CommonResponse<ChatRoomModel> createChatRoom(@RequestBody final ChatRoomDto chatRoomDto) {
		return CommonResponse.success(chatRoomService.createChatRoom(chatRoomDto));
	}

	@GetMapping("/{id}")
	public CommonResponse<ChatRoomModel> getChatRoomById(@PathVariable Long id) {
		return CommonResponse.success(chatRoomService.findById(id));
	}

	@GetMapping
	public CommonResponse<List<ChatRoomModel>> getAllChatRooms() {
		return CommonResponse.success(chatRoomService.findAll());
	}
}
