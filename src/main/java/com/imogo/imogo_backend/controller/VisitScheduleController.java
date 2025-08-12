/*
package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.model.ImobProperty;
import com.imogo.imogo_backend.model.VisitSchedule;
import com.imogo.imogo_backend.repository.AgentRepository;
import com.imogo.imogo_backend.repository.PropertyRepository;
import com.imogo.imogo_backend.repository.VisitScheduleRepository;
import com.imogo.imogo_backend.service.VisitScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/visits")
public class VisitScheduleController {
    @Autowired
    private VisitScheduleService visitScheduleService;

    @Autowired
    private VisitScheduleRepository visitScheduleRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    public ResponseEntity<List<VisitSchedule>> getAllVisits() {
        List<VisitSchedule> visits = visitScheduleRepository.findAll();
        return ResponseEntity.ok(visits);
    }

    @PostMapping
    public ResponseEntity<VisitSchedule> createVisit(@RequestBody VisitSchedule visitSchedule) {
        Agent agent = agentRepository.findById(visitSchedule.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        ImobProperty property = propertyRepository.findById(visitSchedule.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        VisitSchedule response = visitScheduleRepository.save(visitSchedule);
        // notificationService.sendNotificationToAgent(visitSchedule.getAgentId(), "Nova visita agendada!");
        return ResponseEntity.ok().body(response    );
    }
    /*
    @PutMapping("/{id}")
    public ResponseEntity<VisitSchedule> updateVisit(@PathVariable Long id, @RequestBody VisitSchedule visitSchedule) {
        VisitSchedule existingVisit = visitScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        Agent agent = agentRepository.findById(visitSchedule.getAgent().getId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        ImobProperty property = propertyRepository.findById(visitSchedule.getProperty().getId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        existingVisit.setAgent(agent);
        existingVisit.setProperty(property);
        existingVisit.setStartTime(visitSchedule.getStartTime());
        existingVisit.setEndTime(visitSchedule.getEndTime());
        existingVisit.setStatus(visitSchedule.getStatus());
        existingVisit.setNotes(visitSchedule.getNotes());

        VisitSchedule updatedVisit = visitScheduleRepository.save(existingVisit);
        // notificationService.sendNotificationToAgent(agent.getId(), "Visita atualizada!");
        return ResponseEntity.ok(updatedVisit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVisit(@PathVariable Long id) {
        VisitSchedule visit = visitScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        visitScheduleRepository.delete(visit);
        // notificationService.sendNotificationToAgent(visit.getAgent().getId(), "Visita cancelada!");
        return ResponseEntity.ok().body("ok");
    }
} */
