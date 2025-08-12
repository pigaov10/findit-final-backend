package com.imogo.imogo_backend.dto.property_image;

import lombok.Data;


@Data
public class PropertyImageResponseDTO {
    private Long id;
    private byte[] imageData;
}
