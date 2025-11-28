package com.softworld.application.usecase;

import com.softworld.domain.model.Property;
import com.softworld.domain.port.in.GetAllPropertiesUseCase;
import com.softworld.domain.port.out.PropertyRepository;

import java.util.List;

public class GetAllPropertiesUseCaseImpl implements GetAllPropertiesUseCase {

    private final PropertyRepository propertyRepository;

    public GetAllPropertiesUseCaseImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> execute() {
        return propertyRepository.findAll();
    }
}
