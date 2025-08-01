package com.nameless.social.consumer.kafka;

import com.nameless.social.consumer.repository.ChatMessageRepository;
import com.nameless.social.consumer.repository.ChatRoomRepository;
import com.nameless.social.consumer.repository.UserRepository;
import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.User;
import com.nameless.social.core.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageConsumer {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "chat-messages", groupId = "chat-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void listen(ChatMessageDto messageDto) { // Change parameter type to ChatMessageDto
        log.info("Received Message in group chat-consumer-group: {}", messageDto.getMessage());

        // Retrieve ChatRoom and User entities based on IDs from the messageDto
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with ID: " + messageDto.getChatRoomId()));
        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + messageDto.getSenderId()));

        // Create ChatMessage entity from ChatMessageDto
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, messageDto.getMessage());

        chatMessageRepository.save(chatMessage);
    }
}
