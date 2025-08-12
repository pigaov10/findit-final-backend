package com.imogo.imogo_backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.imogo.imogo_backend.model.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Agente que vai acompanhar
    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    // Imóvel a ser visitado
    @ManyToOne(optional = false)
    @JoinColumn(name = "property_id")
    private ImobProperty imobProperty;

    // Data e hora da visita
    @Column(name = "visit_datetime", nullable = false)
    private LocalDateTime visitDatetime;

    // Duração em minutos (ex: 30, 60, 90)
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    // Nome da pessoa que vai visitar
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    // Contato do cliente
    @Column(name = "client_phone", length = 20)
    private String clientPhone;

    @Column(name = "client_email", length = 100)
    private String clientEmail;

    // Notas extras
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Status da visita
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VisitStatus status = VisitStatus.SCHEDULED;

    // Confirmações
    @Column(name = "confirmed_by_client")
    private Boolean confirmedByClient = false;

    @Column(name = "confirmed_by_agent")
    private Boolean confirmedByAgent = false;
}
