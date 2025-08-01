import { APIGatewayProxyHandler } from 'aws-lambda';
import { DynamoDB } from 'aws-sdk';

const dynamoDb = new DynamoDB.DocumentClient();
const connectionsTableName = process.env.CONNECTIONS_TABLE_NAME || 'Connections';

export const handler: APIGatewayProxyHandler = async (event) => {
  const connectionId = event.requestContext.connectionId;
  const { groupId } = JSON.parse(event.body || '{}');

  if (!connectionId || !groupId) {
    return { statusCode: 400, body: 'Connection ID and Group ID are required.' };
  }

  const params = {
    TableName: connectionsTableName,
    Key: { connectionId },
    UpdateExpression: 'set groupId = :g',
    ExpressionAttributeValues: {
      ':g': groupId,
    },
  };

  try {
    await dynamoDb.update(params).promise();
    console.log(`Connection ${connectionId} joined group ${groupId}.`);
    return { statusCode: 200, body: 'Joined group.' };
  } catch (error) {
    console.error('Error joining group:', error);
    return { statusCode: 500, body: 'Failed to join group.' };
  }
};