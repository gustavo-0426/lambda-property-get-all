package com.softworld.hexagonal.application.service;

import com.softworld.hexagonal.domain.model.Property;
import com.softworld.hexagonal.domain.port.input.GetAllPropertiesInputPort;
import com.softworld.hexagonal.domain.port.output.PropertyRepositoryOutputPort;

import java.util.List;

/**
 * USE CASE (Caso de Uso)
 * Implementa la lógica de negocio.
 * - Implementa el puerto de ENTRADA (input port)
 * - Usa el puerto de SALIDA (output port) para datos
 */
public class GetAllPropertiesService implements GetAllPropertiesInputPort {

    private final PropertyRepositoryOutputPort propertyRepository;

    public GetAllPropertiesService(PropertyRepositoryOutputPort propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
}
