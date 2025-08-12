package com.imogo.imogo_backend.dto.agency;

import lombok.Data;

@Data
public class AgencyRequestDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
}
