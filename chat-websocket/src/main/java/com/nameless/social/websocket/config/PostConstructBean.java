package com.nameless.social.websocket.config;

import com.nameless.social.websocket.config.repository.ChatMessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostConstructBean {
	private final ChatMessageRepository chatMessageRepository;

	@PostConstruct
	public void init() {
		chatMessageRepository.createTable();
	}
}
