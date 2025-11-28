package com.softworld.domain.model;

public class Property {

    private long id;
    private long ownerId;
    private String name;
    private String description;
    private String propertyType;
    private double price;
    private boolean available;

    public Property() {
    }

    public Property(long id, long ownerId, String name, String description, 
                    String propertyType, double price, boolean available) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.propertyType = propertyType;
        this.price = price;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
