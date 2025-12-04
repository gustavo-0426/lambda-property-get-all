package com.softworld.hexagonal.domain.port.output;

import com.softworld.hexagonal.domain.model.Property;

import java.util.List;

/**
 * OUTPUT PORT (Puerto de Salida)
 * Define CÓMO obtener datos externos (persistencia, APIs, etc).
 * Lo usa: Caso de uso
 * Lo implementa: Adaptador de salida (MySqlRepository, DynamoDBRepository)
 */
public interface PropertyRepositoryOutputPort {
    List<Property> findAll();
}
