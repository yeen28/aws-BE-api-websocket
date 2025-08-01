package com.nameless.social.websocket.handler;

import com.nameless.social.core.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketChatHandler {
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // TODO: Save chatMessage to database via chat-consumer module
        kafkaTemplate.send("chat-messages", chatMessage); // Kafka 토픽으로 메시지 발행
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage) {
        // TODO: Handle user joining chat room (e.g., add to Redis, notify others)
        kafkaTemplate.send("chat-messages", chatMessage); // Kafka 토픽으로 메시지 발행
    }
}
