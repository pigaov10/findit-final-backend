package com.imogo.imogo_backend.mapper;

import com.imogo.imogo_backend.dto.property.ImobPropertyRequestDTO;
import com.imogo.imogo_backend.dto.property.ImobPropertyResponseDTO;
import com.imogo.imogo_backend.dto.property_agent.PropertyAgentRequestDTO;
import com.imogo.imogo_backend.dto.property_agent.PropertyAgentResponseDTO;
import com.imogo.imogo_backend.dto.property_image.PropertyImageRequestDTO;
import com.imogo.imogo_backend.dto.property_image.PropertyImageResponseDTO;
import com.imogo.imogo_backend.dto.property_location.PropertyLocationRequestDTO;
import com.imogo.imogo_backend.dto.property_location.PropertyLocationResponseDTO;
import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.model.ImobProperty;
import com.imogo.imogo_backend.model.ImobPropertyImage;
import com.imogo.imogo_backend.model.ImobPropertyLocation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImobPropertyMapper {

    @Mappings({
            @Mapping(source = "id", target = "propertyId"),
            @Mapping(source = "title", target = "propertyTitle"),
            @Mapping(source = "description", target = "propertyDescription"),
            @Mapping(source = "price", target = "propertyPrice"),
            @Mapping(source = "status", target = "propertyStatus"),
            @Mapping(source = "bathrooms", target = "propertyBathroom"),
            @Mapping(source = "bedrooms", target = "propertyBedrooms"),
            @Mapping(source = "city", target = "propertyCity"),
            @Mapping(source = "squareFeet", target = "propertySquareFeet"),
            @Mapping(source = "purpose", target = "propertyPurpose"),
            @Mapping(source = "type", target = "propertyType"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "propertyLocation", target = "propertyLocation", qualifiedByName = "toLocationDTO"),
            @Mapping(source = "propertyImages", target = "propertyImages", qualifiedByName = "mapImages"),
            @Mapping(source = "agent", target = "propertyAgent", qualifiedByName = "agentToDTO")
    })
    ImobPropertyResponseDTO toDTO(ImobProperty imobProperty);

    @Mappings({
            @Mapping(source = "propertyTitle", target = "title"),
            @Mapping(source = "propertyDescription", target = "description"),
            @Mapping(source = "propertyPrice", target = "price"),
            @Mapping(source = "propertyBathroom", target = "bathrooms"),
            @Mapping(source = "propertyBedrooms", target = "bedrooms"),
            @Mapping(source = "propertySquareFeet", target = "squareFeet"),
            @Mapping(source = "propertyCity", target = "city"),
            @Mapping(source = "propertyPurpose", target = "purpose"),
            @Mapping(source = "propertyStatus", target = "status"),
            @Mapping(source = "propertyType", target = "type"),
            @Mapping(source = "propertyLocation", target = "propertyLocation", qualifiedByName = "toLocationEntity"),
            @Mapping(source = "propertyImages", target = "propertyImages", qualifiedByName = "toEntityList")
    })
    ImobProperty toEntity(ImobPropertyRequestDTO dto);

    @Named("mapImages")
    default List<PropertyImageResponseDTO> mapImages(List<ImobPropertyImage> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(entity -> {
                    PropertyImageResponseDTO dto = new PropertyImageResponseDTO();
                    dto.setId(entity.getId());
                    dto.setImageData(entity.getImageData());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Named("toEntityList")
    default List<ImobPropertyImage> toEntityList(List<PropertyImageRequestDTO> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(dto -> {
                    ImobPropertyImage image = new ImobPropertyImage();
                    try {
                        image.setImageData(dto.getImageFile().getBytes());
                    } catch (Exception e) {
                        // log ou lançar exceção customizada
                        e.printStackTrace();
                    }
                    return image;
                })
                .collect(Collectors.toList());
    }

    @Named("toLocationDTO")
    default PropertyLocationResponseDTO toLocationDTO(ImobPropertyLocation location) {
        if (location == null) return null;
        PropertyLocationResponseDTO dto = new PropertyLocationResponseDTO();
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setCity(location.getCity());
        dto.setState(location.getState());
        dto.setCountry(location.getCountry());
        dto.setAddress(location.getAddress());
        dto.setZipcode(location.getZipcode());

        return dto;
    }
    @Named("toLocationEntity")
    default ImobPropertyLocation toLocationEntity(PropertyLocationRequestDTO dto) {
        if (dto == null) return null;
        ImobPropertyLocation location = new ImobPropertyLocation();
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setCity(dto.getCity());
        location.setState(dto.getState());
        location.setCountry(dto.getCountry());
        location.setAddress(dto.getAddress());
        location.setZipcode(dto.getZipcode());
        return location;
    }

    @Named("agentToDTO")
    default PropertyAgentResponseDTO agentToDTO(Agent agent) {
        if (agent == null) return null;
        PropertyAgentResponseDTO dto = new PropertyAgentResponseDTO();
        dto.setId(agent.getId());
        dto.setName(agent.getName());
        dto.setEmail(agent.getEmail());
        dto.setPhone(agent.getPhone());
        return dto;
    }

}
