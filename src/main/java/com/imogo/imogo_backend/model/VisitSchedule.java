package com.imogo.imogo_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false)
    private Long agentId; // Changed from Agent object to Long

    @Column(name = "property_id", nullable = false)
    private Long propertyId; // Changed from ImobProperty object to Long

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "status")
    private String status; // Ex: "scheduled", "completed", "canceled"

    @Column(name = "notes")
    private String notes;
}
