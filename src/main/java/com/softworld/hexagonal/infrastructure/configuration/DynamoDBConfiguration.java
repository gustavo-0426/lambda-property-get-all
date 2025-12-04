package com.softworld.hexagonal.infrastructure.configuration;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Configuración de DynamoDB Client
 * Optimizada para AWS Lambda con cliente HTTP ligero
 */
public class DynamoDBConfiguration {

    private static DynamoDbClient dynamoDbClient;
    private static final String DEFAULT_REGION = "us-east-1";
    private static final String TABLE_NAME_ENV = "DYNAMODB_TABLE_NAME";
    private static final String REGION_ENV = "AWS_REGION";

    static {
        String region = System.getenv(REGION_ENV);
        if (region == null || region.isEmpty()) {
            region = DEFAULT_REGION;
        }

        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .httpClient(UrlConnectionHttpClient.builder().build())
                .build();
    }

    public static DynamoDbClient getDynamoDbClient() {
        return dynamoDbClient;
    }

    public static String getTableName() {
        String tableName = System.getenv(TABLE_NAME_ENV);
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalStateException("La variable de entorno DYNAMODB_TABLE_NAME debe estar configurada");
        }
        return tableName;
    }

    public static void close() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
}
