package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.enums.PropertyType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import com.imogo.imogo_backend.dto.property.ImobPropertyRequestDTO;
import com.imogo.imogo_backend.dto.property.ImobPropertyResponseDTO;
import com.imogo.imogo_backend.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/")
    @Operation(summary = "Get all properties", description = "Fetches all properties from the database")
    public Page<ImobPropertyResponseDTO> getAllProperties(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size) {
        return propertyService.getAllProperties(page, size);
    }

    @GetMapping("/ia")
    @Operation(summary = "Get all properties by IA", description = "Fetches all properties from the database")
    public ResponseEntity<String> getAllPropertiesIA(@RequestParam String query) {
        propertyService.getPropertiesByIA(query);
        return ResponseEntity.ok().body("ol");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImobPropertyResponseDTO> getPropertyById(@PathVariable Long id) {
        Optional<ImobPropertyResponseDTO> property = propertyService.getPropertyById(id);
        return property.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @Operation(
        summary = "Save a property",
        description = "Creates a new property with the provided data",
        responses = {
            @ApiResponse(responseCode = "200", description = "Property saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        }
    )
    public ImobPropertyResponseDTO createProperty(@ModelAttribute ImobPropertyRequestDTO property) throws Exception {
        return propertyService.saveProperty(property);
    }

    @GetMapping("/property-types")
    public List<String> getPropertyTypes() {
        return Arrays.stream(PropertyType.values())
                .map(Enum::name) // ou .map(pt -> pt.toString())
                .toList();       // use .collect(Collectors.toList()) se não for Java 16+
    }
}
