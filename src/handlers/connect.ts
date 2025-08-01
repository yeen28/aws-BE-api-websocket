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
    Item: {
      connectionId: connectionId,
    },
  };

  try {
    await dynamoDb.put(params).promise();
    console.log(`Connection ${connectionId} stored.`);
    return { statusCode: 200, body: 'Connected.' };
  } catch (error) {
    console.error('Error storing connection:', error);
    return { statusCode: 500, body: 'Failed to connect.' };
  }
};