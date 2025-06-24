package com.softworld.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Property {

    private long id;
    private long ownerId;
    private String name;
    private String description;
    private String propertyType;
    private double price;
    private boolean available;

}