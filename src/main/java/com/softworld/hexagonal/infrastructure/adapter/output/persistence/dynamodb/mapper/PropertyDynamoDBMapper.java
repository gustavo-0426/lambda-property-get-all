package com.softworld.hexagonal.infrastructure.adapter.output.persistence.dynamodb.mapper;

import com.softworld.hexagonal.domain.model.Property;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper - Convierte entre Property (dominio) y DynamoDB AttributeValue Map
 */
public class PropertyDynamoDBMapper {

    /**
     * Convierte un Map de DynamoDB a un objeto Property del dominio
     */
    public static Property toDomain(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }

        Property property = new Property();
        
        if (item.containsKey("id") && item.get("id").n() != null) {
            property.setId(Long.parseLong(item.get("id").n()));
        }
        if (item.containsKey("ownerId") && item.get("ownerId").n() != null) {
            property.setOwnerId(Long.parseLong(item.get("ownerId").n()));
        }
        if (item.containsKey("name") && item.get("name").s() != null) {
            property.setName(item.get("name").s());
        }
        if (item.containsKey("description") && item.get("description").s() != null) {
            property.setDescription(item.get("description").s());
        }
        if (item.containsKey("propertyType") && item.get("propertyType").s() != null) {
            property.setPropertyType(item.get("propertyType").s());
        }
        if (item.containsKey("price") && item.get("price").n() != null) {
            property.setPrice(Double.parseDouble(item.get("price").n()));
        }
        if (item.containsKey("available") && item.get("available").bool() != null) {
            property.setAvailable(item.get("available").bool());
        }

        return property;
    }

    /**
     * Convierte un objeto Property del dominio a un Map de DynamoDB
     */
    public static Map<String, AttributeValue> toAttributeValueMap(Property property) {
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", AttributeValue.builder().n(String.valueOf(property.getId())).build());
        item.put("ownerId", AttributeValue.builder().n(String.valueOf(property.getOwnerId())).build());
        item.put("name", AttributeValue.builder().s(property.getName()).build());
        item.put("description", AttributeValue.builder().s(property.getDescription()).build());
        item.put("propertyType", AttributeValue.builder().s(property.getPropertyType()).build());
        item.put("price", AttributeValue.builder().n(String.valueOf(property.getPrice())).build());
        item.put("available", AttributeValue.builder().bool(property.isAvailable()).build());

        return item;
    }
}
