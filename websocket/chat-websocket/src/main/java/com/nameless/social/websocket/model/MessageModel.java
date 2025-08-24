package com.nameless.social.websocket.model;

import com.nameless.social.core.entity.ChatMessage;
import com.nameless.social.websocket.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageModel {
	private long clubId;
	private String messageId;
	private String senderEmail;
	private String message;
	private String timestamp;
	private MessageType type;

	public static MessageModel of(final ChatMessage chatMessage) {
		return MessageModel.builder()
				.clubId(chatMessage.getClubId())
				.messageId(chatMessage.getMessageId())
				.senderEmail(String.valueOf(chatMessage.getSenderEmail()))
				.message(chatMessage.getMessage())
				.timestamp(chatMessage.getCreatedAt().toString())
				.type(MessageType.CHAT)
				.build();
	}
}
