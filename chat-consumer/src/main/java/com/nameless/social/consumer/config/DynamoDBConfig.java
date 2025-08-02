//package com.nameless.social.consumer.config;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DynamoDBConfig {
//	@Value("${cloud.aws.dynamodb.endpoint}")
//	private String dynamoDbEndpoint;
//
//	@Value("${cloud.aws.region.static}")
//	private String awsRegion;
//
//	@Value("${cloud.aws.credentials.access-key}")
//	private String awsAccessKey;
//
//	@Value("${cloud.aws.credentials.secret-key}")
//	private String awsSecretKey;
//
//	@Bean
//	public AmazonDynamoDB amazonDynamoDB() {
//		return AmazonDynamoDBClientBuilder.standard()
//				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, awsRegion))
//				.withCredentials(awsCredentialsProvider())
//				.build();
//	}
//
//	@Bean
//	public AWSCredentialsProvider awsCredentialsProvider() {
//		return new AWSStaticCredentialsProvider(awsCredentials());
//	}
//
//	@Bean
//	public AWSCredentials awsCredentials() {
//		return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
//	}
//
//	@Bean
//	public DynamoDBMapper dynamoDBMapper() {
//		return new DynamoDBMapper(amazonDynamoDB());
//	}
//}
