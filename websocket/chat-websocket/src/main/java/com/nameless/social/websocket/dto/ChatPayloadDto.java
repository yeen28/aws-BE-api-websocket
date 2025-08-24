package com.nameless.social.websocket.dto;

import com.nameless.social.websocket.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatPayloadDto {
	private long clubId;
	private String senderEmail;
	private String senderUsername; // Display purposes
	private String message;
	private String timestamp;
	private MessageType type;
}
