import { APIGatewayProxyHandler } from 'aws-lambda';
import { DynamoDB } from 'aws-sdk';

const dynamoDb = new DynamoDB.DocumentClient();
const connectionsTableName = process.env.CONNECTIONS_TABLE_NAME || 'Connections';

export const handler: APIGatewayProxyHandler = async (event) => {
  const connectionId = event.requestContext.connectionId;

  if (!connectionId) {
    return { statusCode: 400, body: 'Connection ID is missing.' };
  }

  const params = {
    TableName: connectionsTableName,
    Key: {
      connectionId: connectionId,
    },
  };

  try {
    await dynamoDb.delete(params).promise();
    console.log(`Connection ${connectionId} removed.`);
    return { statusCode: 200, body: 'Disconnected.' };
  } catch (error) {
    console.error('Error removing connection:', error);
    return { statusCode: 500, body: 'Failed to disconnect.' };
  }
};