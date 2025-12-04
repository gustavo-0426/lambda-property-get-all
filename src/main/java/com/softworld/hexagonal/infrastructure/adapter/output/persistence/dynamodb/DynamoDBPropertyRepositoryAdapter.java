package com.softworld.hexagonal.infrastructure.adapter.output.persistence.dynamodb;

import com.softworld.hexagonal.domain.model.Property;
import com.softworld.hexagonal.domain.port.output.PropertyRepositoryOutputPort;
import com.softworld.hexagonal.infrastructure.adapter.output.persistence.dynamodb.mapper.PropertyDynamoDBMapper;
import com.softworld.hexagonal.infrastructure.configuration.DynamoDBConfiguration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OUTPUT ADAPTER (Adaptador de Salida Secundario)
 * Implementación de DynamoDB para el repositorio de propiedades.
 * - Implementa el puerto de SALIDA (repository)
 * - Convierte datos de DynamoDB a objetos del dominio
 */
public class DynamoDBPropertyRepositoryAdapter implements PropertyRepositoryOutputPort {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public DynamoDBPropertyRepositoryAdapter() {
        this.dynamoDbClient = DynamoDBConfiguration.getDynamoDbClient();
        this.tableName = DynamoDBConfiguration.getTableName();
    }

    @Override
    public List<Property> findAll() {
        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();

            ScanResponse response = dynamoDbClient.scan(scanRequest);
            List<Property> properties = new ArrayList<>();

            for (Map<String, AttributeValue> item : response.items()) {
                Property property = PropertyDynamoDBMapper.toDomain(item);
                if (property != null) {
                    properties.add(property);
                }
            }

            return properties;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener propiedades desde DynamoDB: " + e.getMessage(), e);
        }
    }
}
