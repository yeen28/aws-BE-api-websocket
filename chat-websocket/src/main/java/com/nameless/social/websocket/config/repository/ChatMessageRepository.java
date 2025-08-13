package com.nameless.social.websocket.config.repository;

import com.nameless.social.core.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageRepository {
	private final DynamoDbClient dynamoDbClient;
	private final DynamoDbEnhancedClient enhancedClient;

	private static final String TABLE_NAME = "ChatMessage";

	/**
	 * DynamoDB 테이블 생성
	 */
	public void createTable() {
		try {
			dynamoDbClient.createTable(CreateTableRequest.builder()
					.tableName(TABLE_NAME)
					.attributeDefinitions(
							AttributeDefinition.builder()
									.attributeName("clubId")
									.attributeType(ScalarAttributeType.N)
									.build(),
							AttributeDefinition.builder()
									.attributeName("messageId") // SK
									.attributeType(ScalarAttributeType.S)
									.build(),
							AttributeDefinition.builder()
									.attributeName("senderEmail") // GSI PK
									.attributeType(ScalarAttributeType.S)
									.build()
					)
					.keySchema(
							KeySchemaElement.builder()
									.attributeName("clubId")
									.keyType(KeyType.HASH)
									.build(),
							KeySchemaElement.builder()
									.attributeName("messageId")
									.keyType(KeyType.RANGE)
									.build()
					)
					.globalSecondaryIndexes(
							GlobalSecondaryIndex.builder()
									.indexName("SenderEmailIndex") // GSI 이름
									.keySchema(KeySchemaElement.builder()
											.attributeName("senderEmail")
											.keyType(KeyType.HASH) // GSI PK
											.build()
									)
									.projection(Projection.builder()
											.projectionType(ProjectionType.ALL)
											.build()
									)
									.provisionedThroughput(ProvisionedThroughput.builder()
											.readCapacityUnits(5L)
											.writeCapacityUnits(5L)
											.build()
									)
									.build()
					)
					.provisionedThroughput(
							ProvisionedThroughput.builder()
									.readCapacityUnits(5L)
									.writeCapacityUnits(5L)
									.build()
					)
					.streamSpecification(StreamSpecification.builder()
							.streamEnabled(true)
							// 아래 중 하나 선택
							// streamViewType 은 스트림에 어떤 데이터를 담을지 지정하는 옵션입니다. 보통 NEW_AND_OLD_IMAGES가 많이 쓰입니다.
							.streamViewType(StreamViewType.NEW_AND_OLD_IMAGES)
							// .streamViewType(StreamViewType.NEW_IMAGE)
							// .streamViewType(StreamViewType.KEYS_ONLY)
							// .streamViewType(StreamViewType.OLD_IMAGE)
							.build())
					.build());

			log.info("테이블 생성 완료 - {}", TABLE_NAME);

		} catch (ResourceInUseException e) {
			log.info("테이블이 이미 존재합니다: - {}", TABLE_NAME);
		}
	}

	/**
	 * 데이터 저장 (Enhanced Client 사용)
	 * @param chatMessage
	 */
	public void insertChatMessage(final ChatMessage chatMessage) {
		DynamoDbTable<ChatMessage> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));
		table.putItem(chatMessage);
		log.info(">>> table 저장 완료!!");
	}

	/**
	 * clubId(roomId)로 채팅 메시지 내역 조회
	 * @param clubId
	 * @return
	 */
	public List<ChatMessage> findByClubId(final long clubId) {
		DynamoDbTable<ChatMessage> table =
				enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));

		// PartitionKey(id)가 동일한 모든 아이템 조회
		PageIterable<ChatMessage> pages = table.query(r ->
				r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(clubId)))
		);

		return pages.items().stream().toList();
	}

	public ChatMessage findOneByClubId(final long clubId) {
		DynamoDbTable<ChatMessage> table =
				enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));

		return table.query(r ->
				r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(clubId)))
						.limit(1) // 1건만
		).items().stream().findFirst().orElse(null);
	}

	public ChatMessage findOneByClubIdAndMessageId(final long clubId, final String messageId) {
		DynamoDbTable<ChatMessage> table =
				enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));

		return table.getItem(r -> r
				.key(k -> k.partitionValue(clubId).sortValue(messageId))
		);
	}

	/**
	 * PK/SK: (id, messageId)
	 * GSI: SenderEmailIndex → PK: senderEmail
	 * 이렇게 하면 senderEmail로 바로 검색 가능.
	 * @param email
	 * @return
	 */
	public List<ChatMessage> findBySenderEmail(String email) {
		DynamoDbTable<ChatMessage> table =
				enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));

		DynamoDbIndex<ChatMessage> index = table.index("SenderEmailIndex");

		// 여기서 반환은 SdkIterable<Page<ChatMessage>>
		var sdkIterable = index.query(r -> r.queryConditional(
				QueryConditional.keyEqualTo(k -> k.partitionValue(email))
		));

		// PageIterable로 변환해야 .items() 가능
		PageIterable<ChatMessage> pages = PageIterable.create(sdkIterable);

		return pages.items().stream().toList();
	}

	/**
	 * 이 방식은 DynamoDB 전 테이블을 Scan하는 구조이기 때문에 데이터 양이 많아지면 성능이 급격히 떨어집니다.
	 * 메시지 내용 검색을 본격적으로 쓰려면 OpenSearch 같은 전문 검색엔진 연동이 필요합니다.
	 * @param keyword
	 * @return
	 */
	public List<ChatMessage> searchMessagesByContent(String keyword) {
		DynamoDbTable<ChatMessage> table =
				enhancedClient.table(TABLE_NAME, TableSchema.fromBean(ChatMessage.class));

		Expression filterExpression = Expression.builder()
				.expression("contains(message, :keyword)")
				.expressionValues(Map.of(":keyword", AttributeValue.fromS(keyword)))
				.build();

		return table.scan(r -> r.filterExpression(filterExpression))
				.items()
				.stream()
				.toList();
	}
}
