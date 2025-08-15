package com.nameless.social.websocket.controller;

import com.nameless.social.websocket.model.MessageModel;
import com.nameless.social.websocket.response.CommonResponse;
import com.nameless.social.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ws")
@RequiredArgsConstructor
public class ChatHistoryController {
	private final ChatMessageService chatMessageService;

	@GetMapping("/{clubId}/messages")
	public CommonResponse<List<MessageModel>> getMessagesByClubId(
			@PathVariable("clubId") final long clubId
	) {
		return CommonResponse.success(chatMessageService.findMessagesByClubId(clubId));
	}

	@GetMapping("/{clubId}/message")
	public CommonResponse<MessageModel> getMessageByClubId(
			@PathVariable("clubId") final long clubId
	) {
		return CommonResponse.success(chatMessageService.findOneByClubId(clubId));
	}

	@GetMapping("/{clubId}/messages/{messageId}")
	public CommonResponse<MessageModel> getMessageByClubIdAndMessageId(
			@PathVariable("clubId") final long clubId,
			@PathVariable("messageId") final String messageId
	) {
		return CommonResponse.success(chatMessageService.findOneByClubIdAndMessageId(clubId, messageId));
	}

	// club이 삭제되면 해당 club의 메시지들도 모두 제거합니다.
	@DeleteMapping("/{clubId}")
	public CommonResponse<Object> deleteMessages() {
//		chatMessageService.delete;
		return CommonResponse.success(HttpStatus.OK);
	}
}