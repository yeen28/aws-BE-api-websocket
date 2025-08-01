import { APIGatewayProxyHandler } from 'aws-lambda';
import { DynamoDB } from 'aws-sdk';
import { v4 as uuidv4 } from 'uuid';

const dynamoDb = new DynamoDB.DocumentClient();
const messagesTableName = process.env.MESSAGES_TABLE_NAME || 'Messages';
const connectionsTableName = process.env.CONNECTIONS_TABLE_NAME || 'Connections';

export const handler: APIGatewayProxyHandler = async (event) => {
  const connectionId = event.requestContext.connectionId;
  const { message } = JSON.parse(event.body || '{}');

  if (!connectionId || !message) {
    return { statusCode: 400, body: 'Connection ID and message are required.' };
  }

  try {
    // Get groupId from the connection
    const connection = await dynamoDb.get({ TableName: connectionsTableName, Key: { connectionId } }).promise();
    const groupId = connection.Item?.groupId;

    if (!groupId) {
      return { statusCode: 400, body: 'You are not in a group.' };
    }

    const messageId = uuidv4();
    const createdAt = new Date().toISOString();

    const params = {
      TableName: messagesTableName,
      Item: {
        messageId,
        groupId,
        message,
        sender: connectionId,
        createdAt,
      },
    };

    await dynamoDb.put(params).promise();

    return { statusCode: 200, body: 'Message sent.' };
  } catch (error) {
    console.error('Error sending message:', error);
    return { statusCode: 500, body: 'Failed to send message.' };
  }
};