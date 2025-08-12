package com.imogo.imogo_backend.dto.property_location;

import lombok.Data;

@Data
public class PropertyLocationRequestDTO {
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
