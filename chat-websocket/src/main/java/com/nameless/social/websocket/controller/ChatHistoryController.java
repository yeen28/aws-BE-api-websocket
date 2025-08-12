package com.nameless.social.websocket.controller;

import com.nameless.social.websocket.model.MessageModel;
import com.nameless.social.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/chat/{clubId}/message")
	public ResponseEntity<MessageModel> getMessageByClubId(
			@PathVariable("clubId") final long clubId
	) {
		return ResponseEntity.ok(chatMessageService.findOneByClubId(clubId));
	}

	@GetMapping("/chat/{clubId}/messages/{messageId}")
	public ResponseEntity<MessageModel> getMessageByClubIdAndMessageId(
			@PathVariable("clubId") final long clubId,
			@PathVariable("messageId") final String messageId
	) {
		return ResponseEntity.ok(chatMessageService.findOneByClubIdAndMessageId(clubId, messageId));
	}
}