package com.nameless.social.websocket.service;

import com.nameless.social.websocket.dto.ChatPayloadDto;
import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.websocket.model.MessageModel;
import com.nameless.social.websocket.config.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;

	public void save(final ChatMessage message) {
		log.info("message save start: {} {}", message.getClubId(), message.getSenderEmail());
		chatMessageRepository.insertChatMessage(message);
		log.info("message saved to DynamoDB: {}", message.getMessage());
	}

	public List<MessageModel> findMessagesByClubId(final long clubId) {
		List<ChatMessage> chatMessage = chatMessageRepository.findByClubId(clubId);
		return chatMessage.stream()
				.map(MessageModel::of)
				.toList();
	}

	/**
	 * clubId를 기준으로 조회했을 때 가장 처음 대화만 조회.
	 * -> 채팅방 목록 보여줄 때 최근 메시지만 미리보기로 제공할 때 사용할 수 있음.
	 * @param clubId
	 * @return
	 */
	public MessageModel findOneByClubId(final long clubId) {
		ChatMessage chatMessage = chatMessageRepository.findOneByClubId(clubId);
		return MessageModel.of(chatMessage);
	}

	/**
	 * clubId, messageId로 메시지 내역 조회
	 * @param clubId
	 * @param messageId
	 * @return
	 */
	public MessageModel findOneByClubIdAndMessageId(final long clubId, final String messageId) {
		ChatMessage chatMessage = chatMessageRepository.findOneByClubIdAndMessageId(clubId, messageId);
		return MessageModel.of(chatMessage);
	}
}