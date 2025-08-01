import { DynamoDBStreamHandler } from 'aws-lambda';
import { DynamoDB, ApiGatewayManagementApi } from 'aws-sdk';

const dynamoDb = new DynamoDB.DocumentClient();
const connectionsTableName = process.env.CONNECTIONS_TABLE_NAME || 'Connections';
const apiGwManagementApi = new ApiGatewayManagementApi({
  apiVersion: '2018-11-29',
  endpoint: process.env.WEBSOCKET_API_ENDPOINT,
});

export const handler: DynamoDBStreamHandler = async (event) => {
  for (const record of event.Records) {
    if (record.eventName === 'INSERT' && record.dynamodb?.NewImage) {
      const newMessage = DynamoDB.Converter.unmarshall(record.dynamodb.NewImage);
      const { groupId, ...messageDetails } = newMessage;

      const params = {
        TableName: connectionsTableName,
        IndexName: 'GroupIdIndex',
        KeyConditionExpression: 'groupId = :g',
        ExpressionAttributeValues: {
          ':g': groupId,
        },
      };

      try {
        const connections = await dynamoDb.query(params).promise();
        const postCalls = connections.Items?.map(async ({ connectionId }) => {
          try {
            await apiGwManagementApi.postToConnection({ ConnectionId: connectionId, Data: JSON.stringify(messageDetails) }).promise();
          } catch (e: any) {
            if (e.statusCode === 410) {
              console.log(`Found stale connection, deleting ${connectionId}`);
              await dynamoDb.delete({ TableName: connectionsTableName, Key: { connectionId } }).promise();
            } else {
              throw e;
            }
          }
        });

        if (postCalls) {
          await Promise.all(postCalls);
        }
      } catch (error) {
        console.error('Error fanning out message:', error);
      }
    }
  }
};