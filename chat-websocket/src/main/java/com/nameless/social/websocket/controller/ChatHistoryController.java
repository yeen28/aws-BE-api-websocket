package com.nameless.social.websocket.controller;

import com.nameless.social.websocket.model.MessageModel;
import com.nameless.social.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatHistoryController {
	private final ChatMessageService chatMessageService;

	@GetMapping("/{clubId}/messages")
	public ResponseEntity<List<MessageModel>> getMessagesByClubId(
			@PathVariable("clubId") final long clubId
	) {
		return ResponseEntity.ok(chatMessageService.findMessagesByClubId(clubId));
	}

	@GetMapping("/{clubId}/message")
	public ResponseEntity<MessageModel> getMessageByClubId(
			@PathVariable("clubId") final long clubId
	) {
		return ResponseEntity.ok(chatMessageService.findOneByClubId(clubId));
	}

	@GetMapping("/{clubId}/messages/{messageId}")
	public ResponseEntity<MessageModel> getMessageByClubIdAndMessageId(
			@PathVariable("clubId") final long clubId,
			@PathVariable("messageId") final String messageId
	) {
		return ResponseEntity.ok(chatMessageService.findOneByClubIdAndMessageId(clubId, messageId));
	}

	// club이 삭제되면 해당 club의 메시지들도 모두 제거합니다.
	@DeleteMapping("/{clubId}")
	public ResponseEntity<Object> deleteMessages() {
//		chatMessageService.delete;
		return ResponseEntity.ok(HttpStatus.OK);
	}
}