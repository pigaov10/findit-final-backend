package com.imogo.imogo_backend.dto.property;

import com.imogo.imogo_backend.dto.property_agent.PropertyAgentResponseDTO;
import com.imogo.imogo_backend.dto.property_image.PropertyImageRequestDTO;
import com.imogo.imogo_backend.dto.property_image.PropertyImageResponseDTO;
import com.imogo.imogo_backend.dto.property_location.PropertyLocationResponseDTO;
import com.imogo.imogo_backend.model.enums.ImobPropertyType;
import com.imogo.imogo_backend.model.enums.ImobStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImobPropertyResponseDTO {
    private Long propertyId;
    private String propertyTitle;
    private String propertyDescription;
    private String propertyCity;
    private BigDecimal propertyPrice;
    private Integer propertyBathroom;
    private Integer propertyBedrooms;
    private Integer propertySquareFeet;
    private String propertyPurpose;
    private ImobStatus propertyStatus;
    private ImobPropertyType propertyType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PropertyImageResponseDTO> propertyImages;
    private PropertyLocationResponseDTO propertyLocation;
    private PropertyAgentResponseDTO  propertyAgent;
}