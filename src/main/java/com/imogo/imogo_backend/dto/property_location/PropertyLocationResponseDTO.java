package com.imogo.imogo_backend.dto.property_location;

import lombok.Data;

@Data
public class PropertyLocationResponseDTO {
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
