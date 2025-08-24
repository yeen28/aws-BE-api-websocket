package com.nameless.social.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {
	@Value("${aws.dynamodb.region}")
	private String region;

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder()
				.region(Region.of(region))
				// EKS와 Pod에 IAM Role을 연결하면 자동으로 인증됨 (IRSA 방식)
				 .credentialsProvider(DefaultCredentialsProvider.create())
//				.credentialsProvider(ProfileCredentialsProvider.create("nameless-dev")) // IAM 역할이나 환경변수 사용.  // CLI 프로필 사용
				.build();
	}

	@Bean
	public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
		return DynamoDbEnhancedClient.builder()
				.dynamoDbClient(dynamoDbClient)
				.build();
	}
}