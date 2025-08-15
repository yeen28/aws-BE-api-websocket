package com.nameless.social.websocket.service;

import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.websocket.client.ChatApiClient;
import com.nameless.social.websocket.config.repository.ChatMessageRepository;
import com.nameless.social.websocket.dto.ChatPayloadDto;
import com.nameless.social.websocket.model.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final ChatApiClient chatApiClient;

	public void save(final ChatMessage message) {
		log.info("message save start: {} {}", message.getClubId(), message.getSenderEmail());
		chatMessageRepository.insertChatMessage(message);
		log.info("message saved to DynamoDB: {}", message.getMessage());
	}

	public List<MessageModel> findMessagesByClubId(final long clubId) {
		List<ChatMessage> chatMessages = chatMessageRepository.findByClubId(clubId);
		return chatMessages.stream()
				.map(MessageModel::of)
				.toList();
	}

	public MessageModel findOneByClubId(final long clubId) {
		ChatMessage chatMessage = chatMessageRepository.findOneByClubId(clubId);
		return MessageModel.of(chatMessage);
	}

	public MessageModel findOneByClubIdAndMessageId(final long clubId, final String messageId) {
		ChatMessage chatMessage = chatMessageRepository.findOneByClubIdAndMessageId(clubId, messageId);
		return MessageModel.of(chatMessage);
	}

	/**
	 * 사용자가 club에 가입하면 AI에 정보를 전달해서 퀘스트 생성
	 */
	public void sendUserInfoToAI(final ChatPayloadDto dto) {
		chatApiClient.getUserIdByEmail(dto.getSenderEmail())
				.flatMap(extractedUserId -> {
					log.info("Extracted User ID: {}", extractedUserId);

					// TODO: AI 서비스 호출 로직 구현
					long userId = extractedUserId;
					long clubId = dto.getClubId();

					return Mono.empty();
				})
				.onErrorResume(error -> {
					// 최종 에러 처리 (ChatApiClient에서 이미 상세 로그를 남겼으므로 여기서는 간단히 로깅)
					log.error("Error in sendUserInfoToAI for email: {}. Process will not continue.", dto.getSenderEmail());
					return Mono.empty(); // 스트림을 안전하게 종료
				})
				.subscribe();
	}
}