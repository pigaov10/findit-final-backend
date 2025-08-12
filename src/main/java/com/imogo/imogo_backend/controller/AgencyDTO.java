package com.imogo.imogo_backend.controller;

import lombok.Data;

import java.util.List;

@Data
public class AgencyDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String district;
    private String postalCode;
    private String website;
    private String contactPerson;
    private String notes;
    private String status;

    private List<AgentDTO> agents;
}
