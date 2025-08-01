package com.nameless.social.consumer.kafka;

import com.nameless.social.consumer.repository.ChatMessageRepository;
import com.nameless.social.consumer.repository.ChatRoomRepository;
import com.nameless.social.consumer.repository.UserRepository;
import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.core.entity.ChatRoom;
import com.nameless.social.core.entity.User;
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
    public void listen(ChatMessage message) {
        log.info("Received Message in group chat-consumer-group: {}", message.getMessage());

        // Retrieve ChatRoom and User entities based on IDs from the message
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with ID: " + message.getChatRoomId()));
        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + message.getSenderId()));

        // Set the retrieved entities to the message object
        message.setChatRoom(chatRoom);
        message.setSender(sender);

        chatMessageRepository.save(message);
    }
}
