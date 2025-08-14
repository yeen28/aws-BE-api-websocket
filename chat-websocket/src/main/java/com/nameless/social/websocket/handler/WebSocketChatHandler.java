package com.nameless.social.websocket.handler;

import com.nameless.social.websocket.dto.ChatPayloadDto;
import com.nameless.social.websocket.enums.MessageType;
import com.nameless.social.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler {
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatMessageService chatMessageService;

	/**
	 * 이미지의 경우: data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAYAAAC…
	 * @param chatPayloadDto
	 * @param headers
	 */
	@MessageMapping("/chat.sendMessage")
	public void sendMessage(
			@Payload ChatPayloadDto chatPayloadDto,
			@Header(required = false) Map<String, Object> headers
	) {
		log.info("Received message: {} - {}", chatPayloadDto.getSenderEmail(), chatPayloadDto.getMessage());

		String contentType = "";
		if (!Objects.isNull(headers)) {
			contentType = (String) headers.get("content-type");
		}

		if ("image/base64".equals(contentType)) {
			// 이미지인 경우 그대로 전달 (content-type 포함)
			messagingTemplate.convertAndSend(
					"/topic/chatroom/" + chatPayloadDto.getClubId(),
					chatPayloadDto,
					Map.of("content-type", "image/base64"));
		} else {
			// 일반 메시지
			messagingTemplate.convertAndSend(
					"/topic/chatroom/" + chatPayloadDto.getClubId(),
					chatPayloadDto
			);
		}

		if (chatPayloadDto.getType() == MessageType.CHAT) {
			// DynamoDB에 채팅 메시지 저장
			chatMessageService.createTable(); // TODO DynamoDB 접근을 매번 하지 않도록 수정 필요. 서버 처음 구동시 테이블 생성하는 코드부터 한 번만 호출하도록 하기.
			chatMessageService.save(chatPayloadDto);
		}

		// 채팅방 구독자들에게 메시지 전송
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatPayloadDto.getClubId(), chatPayloadDto);
	}

	@MessageMapping("/chat.addUser")
	public void addUser(@Payload ChatPayloadDto chatPayloadDto) {
		log.info("User joined: {}", chatPayloadDto.getSenderEmail());
		messagingTemplate.convertAndSend("/topic/chatroom/" + chatPayloadDto.getClubId(), chatPayloadDto);
	}
}
