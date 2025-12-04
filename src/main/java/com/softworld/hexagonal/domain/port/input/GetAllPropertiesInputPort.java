package com.softworld.hexagonal.domain.port.input;

import com.softworld.hexagonal.domain.model.Property;

import java.util.List;

/**
 * INPUT PORT (Puerto de Entrada)
 * Define QUÉ operaciones puede hacer el sistema.
 * Lo invoca: Adaptador de entrada (Lambda, REST API, CLI)
 * Lo implementa: Caso de uso en application/service
 */
public interface GetAllPropertiesInputPort {
    List<Property> getAllProperties();
}
