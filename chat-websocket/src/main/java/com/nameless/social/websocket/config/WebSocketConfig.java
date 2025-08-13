package com.nameless.social.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/topic/chatroom"); // 메시지를 구독하는 클라이언트에게 메시지를 전달할 prefix
		config.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 메시지를 발행할 때 사용할 prefix
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat/ws") // 클라이언트에서 WebSocket 연결을 위한 엔드포인트
				.setAllowedOrigins("*")
				.withSockJS();
	}
}
