package com.softworld.domain.port.in;

import com.softworld.domain.model.Property;

import java.util.List;

public interface GetAllPropertiesUseCase {
    List<Property> execute();
}
