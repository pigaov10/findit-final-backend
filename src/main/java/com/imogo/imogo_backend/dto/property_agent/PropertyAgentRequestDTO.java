package com.imogo.imogo_backend.dto.property_agent;

import lombok.Data;

@Data
public class PropertyAgentRequestDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Long agencyId;
}
