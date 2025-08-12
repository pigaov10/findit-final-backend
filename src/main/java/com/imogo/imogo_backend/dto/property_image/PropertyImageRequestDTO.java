package com.imogo.imogo_backend.dto.property_image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class PropertyImageRequestDTO {
    private Long id;
    private MultipartFile imageFile;
}
