package com.imogo.imogo_backend.dto;

import lombok.Data;

@Data
public class PropertySearchDTO {
    private String type;
    private String purpose; // "venda", "aluguel"
    private String city;
    private Integer bedrooms;
}
