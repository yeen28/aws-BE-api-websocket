package com.nameless.social.websocket.handler;

import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.websocket.dto.ChatPayloadDto;
import com.nameless.social.websocket.enums.MessageType;
import com.nameless.social.websocket.service.ChatMessageService;
import com.nameless.social.websocket.utils.MediaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler {
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatMessageService chatMessageService;

	/**
	 * @param chatPayloadDto
	 * @param headers
	 */
	@MessageMapping("/chat.sendMessage")
	public void sendMessage(
			@Payload ChatPayloadDto chatPayloadDto,
			@Header(required = false) Map<String, Object> headers
	) {
		log.info("Received message: {} - {}", chatPayloadDto.getSenderEmail(), chatPayloadDto.getMessage());

		String imgType = "";
		if (MessageType.IMAGE.equals(chatPayloadDto.getType())) {
			imgType = MediaUtils.getType(chatPayloadDto.getMessage());
		}

		// JOIN, LEAVE가 아닐 때만 DynamoDB에 채팅 내역 저장
		List<MessageType> notSaveTypes = List.of(MessageType.JOIN, MessageType.LEAVE);
		if (!notSaveTypes.contains(chatPayloadDto.getType())) {
			chatMessageService.save(
					ChatMessage.builder()
							.clubId(chatPayloadDto.getClubId())
							.messageId(String.format("%s#%s", Instant.now().toString(), UUID.randomUUID()))
							.senderEmail(chatPayloadDto.getSenderEmail())
							.message(chatPayloadDto.getMessage())
							.messageType(chatPayloadDto.getType().name())
							.imageType(StringUtils.hasText(imgType) ? imgType : "")
							.createdAt(LocalDateTime.now())
							.build()
			);
		}

		// 채팅방 구독자들에게 메시지 전송
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatPayloadDto.getClubId(), chatPayloadDto);
	}

	@MessageMapping("/chat.addUser")
	public void addUser(@Payload ChatPayloadDto chatPayloadDto) {
		log.info("User joined: {}", chatPayloadDto.getSenderEmail());
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatPayloadDto.getClubId(), chatPayloadDto);

		chatMessageService.createTodayQuestIfAbsent(chatPayloadDto);
	}
}
