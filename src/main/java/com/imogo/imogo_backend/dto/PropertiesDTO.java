package com.imogo.imogo_backend.dto;

import com.imogo.imogo_backend.model.enums.PropertyStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PropertiesDTO {
    private String propertyId;
    private String propertyTitle;
    private String propertyDescription;
    private BigDecimal propertyPrice;
    private PropertyStatus propertyStatus;
    private Integer propertyBathroom;
    private List<String> propertyImages;
    private LatLngDTO propertyLocation;
}
