package com.nameless.social.websocket.handler;

import com.nameless.social.core.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler {
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(@Payload ChatMessageDto chatMessage) {
		log.info("Received message: {}", chatMessage);
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatMessage.getChatRoomId(), chatMessage);
	}

	@MessageMapping("/chat.addUser")
	public void addUser(@Payload ChatMessageDto chatMessage) {
		log.info("User joined: {}", chatMessage);
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatMessage.getChatRoomId(), chatMessage);
	}
}
