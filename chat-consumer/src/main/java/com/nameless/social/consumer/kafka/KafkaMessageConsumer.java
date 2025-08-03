//package com.nameless.social.consumer.kafka;
//
//import com.nameless.social.consumer.repository.ChatMessageRepository;
//import com.nameless.social.core.entity.ChatMessage;
//import com.nameless.social.core.dto.ChatMessageDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class KafkaMessageConsumer {
//	private final ChatMessageRepository chatMessageRepository;
//
//	@KafkaListener(topics = "chat-messages", groupId = "chat-consumer-group", containerFactory = "kafkaListenerContainerFactory")
//	public void listen(ChatMessageDto messageDto) {
//		log.info("Received Message in group chat-consumer-group: {}", messageDto.getMessage());
//
//		// Create ChatMessage entity from ChatMessageDto
//		ChatMessage chatMessage = new ChatMessage(messageDto.getChatRoomId(), messageDto.getSenderId(), messageDto.getMessage());
//
//		chatMessageRepository.save(chatMessage);
//	}
//}