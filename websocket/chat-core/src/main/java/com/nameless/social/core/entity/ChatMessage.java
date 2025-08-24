package com.nameless.social.core.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@DynamoDbBean
@Setter
@NoArgsConstructor // TODO 꼭 있어야 함. social.core.entity.ChatMessage' appears to have no default constructor thus cannot be used with the BeanTableSchema
@AllArgsConstructor
@Builder
public class ChatMessage {
	private long clubId;
	private String messageId;
	private String senderEmail;
	@Getter private String message;
	@Getter private String messageType;
	@Getter private String imageType;
	@Getter private LocalDateTime createdAt;

	@DynamoDbPartitionKey
	public long getClubId() {
		return clubId;
	}

	@DynamoDbSortKey
	public String getMessageId() {
		return messageId;
	}

	@DynamoDbSecondaryPartitionKey(indexNames = "SenderEmailIndex")
	public String getSenderEmail() {
		return senderEmail;
	}
}
