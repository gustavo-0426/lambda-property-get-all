package com.softworld.util;

import com.softworld.model.Property;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyMapper {

    public static Property mapFromResultSet(ResultSet rs) throws SQLException {
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
