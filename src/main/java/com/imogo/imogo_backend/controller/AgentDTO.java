package com.imogo.imogo_backend.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgentDTO {

    private Long id;

    @NotBlank
    private String name;

    private String licenseNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String taxId;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String district;

    private String postalCode;

    private String website;

    private String contactPerson;

    private String notes;

    @NotBlank
    private String status; // "active" | "inactive"

    @NotBlank
    private AgencyDTO agency; // Só o ID da agência, para evitar desserialização complicada
}
