//package com.nameless.social.consumer.repository;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.nameless.social.core.entity.ChatMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class ChatMessageRepository {
//	private final DynamoDBMapper dynamoDBMapper;
//
//	public ChatMessage save(ChatMessage chatMessage) {
//		dynamoDBMapper.save(chatMessage);
//		return chatMessage;
//	}
//}
