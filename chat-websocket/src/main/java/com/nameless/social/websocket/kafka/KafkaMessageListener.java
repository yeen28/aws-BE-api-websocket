package com.nameless.social.websocket.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.core.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageListener {
	private final SimpMessagingTemplate messagingTemplate;
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@KafkaListener(topics = "chat-messages", groupId = "websocket-group")
	public void listen(String message) {
		log.info("Received message from Kafka: {}", message);

		ChatMessageDto chatMessageDto = null;
		try {
			chatMessageDto = OBJECT_MAPPER.readValue(message, ChatMessageDto.class);

			// 특정 채팅방으로 메시지 전송
			messagingTemplate.convertAndSend("/topic/chatroom/" + chatMessageDto.getChatRoomId(), chatMessageDto);

		} catch (JsonProcessingException e) {
			log.warn("메시지 역직렬화 실패", e);
		}
	}
}
