package com.softworld.hexagonal.infrastructure.adapter.output.persistence.mysql;

import com.softworld.hexagonal.domain.model.Property;
import com.softworld.hexagonal.domain.port.output.PropertyRepositoryOutputPort;
import com.softworld.hexagonal.infrastructure.adapter.output.persistence.mysql.mapper.PropertyMySqlMapper;
import com.softworld.hexagonal.infrastructure.configuration.MySqlDatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * OUTPUT ADAPTER (Adaptador de Salida Secundario)
 * Implementa acceso a datos con MySQL.
 * - Implementa el puerto de SALIDA (output port)
 * - Usa tecnología específica (JDBC, MySQL)
 * - Convierte datos de BD a entidades del dominio
 */
public class MySqlPropertyRepositoryAdapter implements PropertyRepositoryOutputPort {

    private static final String FIND_ALL_QUERY = 
        "SELECT id, owner_id, name, description, property_type, price, available FROM property";

    @Override
    public List<Property> findAll() {
        List<Property> properties = new ArrayList<>();

        try (Connection connection = MySqlDatabaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                properties.add(PropertyMySqlMapper.toDomain(resultSet));
            }

            return properties;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching properties from MySQL database", e);
        }
    }
}
