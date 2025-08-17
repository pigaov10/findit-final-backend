package com.imogo.imogo_backend.events;

import com.imogo.imogo_backend.model.enums.EventType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PropertyEvent {
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String city;
    private String district;
    private String propertyType;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;
    private Integer yearBuilt;
    private String condition;
    private List<String> features;
    private Boolean furnished;
    private Integer floor;
    private String energyCertificate;
    private Double latitude;
    private Double longitude;
    private List<String> nearby;

    private EventType type; // CREATED, UPDATED, DELETED
}
