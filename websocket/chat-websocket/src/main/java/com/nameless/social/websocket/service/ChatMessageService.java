package com.nameless.social.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.websocket.client.ChatApiClient;
import com.nameless.social.websocket.config.repository.ChatMessageRepository;
import com.nameless.social.websocket.dto.ChatPayloadDto;
import com.nameless.social.websocket.dto.GenerateQuestDto;
import com.nameless.social.websocket.dto.GenerateQuestModel;
import com.nameless.social.websocket.dto.InsertQuestDto;
import com.nameless.social.websocket.model.MessageModel;
import com.nameless.social.websocket.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ChatApiClient chatApiClient;

	@Value("${services.ai.url}")
	private String aiUrl;

	@Value("${services.chat-api.url}")
	private String apiUrl;

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
	 * 사용자가 club에 접속했을 때, 오늘 생성된 퀘스트가 없다면 퀘스트 생성 요청
	 */
	public void createTodayQuestIfAbsent(final ChatPayloadDto dto) {
		CommonResponse response = null;
		try {
			response = restTemplate.getForObject(
					String.format("%s/api/club/%s/existQuest", apiUrl, dto.getClubId()),
					CommonResponse.class
			);

		} catch (Exception e) {
			log.error("Error calling API: {} {} {}", dto.getClubId(), dto.getSenderEmail(), e.getMessage());
		}

		if (Boolean.TRUE.equals(response.getData())) {
			log.info("오늘의 퀘스트가 이미 존재하므로 AI 호출을 건너뜁니다.");
			return;
		}

		GenerateQuestModel generateQuestModel = null;
		try {
			GenerateQuestDto generateQuestDto = new GenerateQuestDto(dto.getSenderEmail(), String.valueOf(dto.getClubId()), "title");
			generateQuestModel = restTemplate.postForObject(
					String.format("%s/generateQuest", aiUrl),
					generateQuestDto,
					GenerateQuestModel.class
			);
			log.info("Successfully called AI API. Response for user {}: {}", generateQuestModel.getUser(), objectMapper.writeValueAsString(generateQuestModel.getQuestList()));
		} catch (Exception e) {
			log.error("Error calling API: {} {} {}", dto.getClubId(), dto.getSenderEmail(), e.getMessage());
		}

		try {
			List<InsertQuestDto> insertQuestDtoList = new ArrayList<>();
			generateQuestModel.getQuestList().forEach(questModel -> {
				insertQuestDtoList.add(new InsertQuestDto(
						questModel.getQuestTitle(),
						questModel.getQuestDescription(),
						questModel.getDifficulty()
				));
			});

			restTemplate.postForObject(
					String.format("%s/api/club/%s/quest", apiUrl, dto.getClubId()),
					insertQuestDtoList,
					CommonResponse.class
			);

		} catch (Exception e) {
			log.error("Error calling API: {} {} {}", dto.getClubId(), dto.getSenderEmail(), e.getMessage());
		}
	}

	/**
	 * 사용자가 club에 가입하면 AI에 정보를 전달해서 퀘스트 생성
	 */
//	public void createTodayQuestIfAbsent(final ChatPayloadDto dto) {
//		chatApiClient.getUserIdByEmail(dto.getSenderEmail())
//				.flatMap(extractedUserId -> {
//					log.info("Extracted User ID: {}", extractedUserId);
//
//					// TODO: AI 서비스 호출 로직 구현
//					long userId = extractedUserId;
//					long clubId = dto.getClubId();
//
//					return Mono.empty();
//				})
//				.onErrorResume(error -> {
//					// 최종 에러 처리 (ChatApiClient에서 이미 상세 로그를 남겼으므로 여기서는 간단히 로깅)
//					log.error("Error in sendUserInfoToAI for email: {}. Process will not continue.", dto.getSenderEmail());
//					return Mono.empty(); // 스트림을 안전하게 종료
//				})
//				.subscribe();
//	}
}