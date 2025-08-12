package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.enums.VisitStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDTO {

    private Long id;

    @NotNull(message = "Agent ID is required")
    private Long agentId;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "title is required")
    private String title;

    @NotNull(message = "Visit date and time is required")
    @Future(message = "Visit date must be in the future")
    private LocalDateTime visitDatetime;

    @NotNull(message = "Duration is required")
    private Integer durationMinutes;

    @NotBlank(message = "Client name is required")
    private String clientName;

    private String clientPhone;

    @Email(message = "Invalid client email")
    private String clientEmail;

    private String notes;

    private VisitStatus status = VisitStatus.SCHEDULED;

    private Boolean confirmedByClient = false;

    private Boolean confirmedByAgent = false;
}
