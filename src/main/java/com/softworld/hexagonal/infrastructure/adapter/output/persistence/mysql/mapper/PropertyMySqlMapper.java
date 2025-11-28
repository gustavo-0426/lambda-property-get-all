package com.softworld.hexagonal.infrastructure.adapter.output.persistence.mysql.mapper;

import com.softworld.hexagonal.domain.model.Property;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MAPPER
 * Convierte datos de MySQL (ResultSet) a entidades del dominio (Property).
 */
public class PropertyMySqlMapper {

    public static Property toDomain(ResultSet rs) throws SQLException {
        Property property = new Property();
        property.setId(rs.getLong("id"));
        property.setOwnerId(rs.getLong("owner_id"));
        property.setName(rs.getString("name"));
        property.setDescription(rs.getString("description"));
        property.setPropertyType(rs.getString("property_type"));
        property.setPrice(rs.getDouble("price"));
        property.setAvailable(rs.getBoolean("available"));
        return property;
    }
}
