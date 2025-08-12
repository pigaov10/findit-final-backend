package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.Visit;


import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.model.ImobProperty;
import com.imogo.imogo_backend.repository.AgentRepository;
import com.imogo.imogo_backend.repository.PropertyRepository;
import com.imogo.imogo_backend.repository.VisitRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitRepository visitRepository;
    private final AgentRepository agentRepository;
    private final PropertyRepository propertyRepository;

    // ✅ LIST ALL VISITS
    @GetMapping
    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    // ✅ GET VISIT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable Long id) {
        Optional<Visit> visit = visitRepository.findById(id);
        return visit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ CREATE VISIT
    @PostMapping
    public ResponseEntity<Visit> createVisit(@Valid @RequestBody VisitDTO dto) {
        Optional<Agent> agent = agentRepository.findById(dto.getAgentId());
        Optional<ImobProperty> property = propertyRepository.findById(dto.getPropertyId());

        if (agent.isEmpty() || property.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Visit visit = new Visit();
        visit.setAgent(agent.get());
        visit.setImobProperty(property.get());
        visit.setVisitDatetime(dto.getVisitDatetime());
        visit.setDurationMinutes(dto.getDurationMinutes());
        visit.setClientName(dto.getClientName());
        visit.setClientPhone(dto.getClientPhone());
        visit.setClientEmail(dto.getClientEmail());
        visit.setNotes(dto.getNotes());
        visit.setStatus(dto.getStatus());
        visit.setTitle(dto.getTitle());
        visit.setConfirmedByAgent(dto.getConfirmedByAgent());
        visit.setConfirmedByClient(dto.getConfirmedByClient());

        Visit savedVisit = visitRepository.save(visit);
        return ResponseEntity.ok(savedVisit);
    }

    // ✅ UPDATE VISIT
    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable Long id, @Valid @RequestBody VisitDTO dto) {
        Optional<Visit> existingVisit = visitRepository.findById(id);
        Optional<Agent> agent = agentRepository.findById(dto.getAgentId());
        Optional<ImobProperty> property = propertyRepository.findById(dto.getPropertyId());

        if (existingVisit.isEmpty() || agent.isEmpty() || property.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Visit visit = existingVisit.get();
        visit.setAgent(agent.get());
        visit.setImobProperty(property.get());
        visit.setVisitDatetime(dto.getVisitDatetime());
        visit.setDurationMinutes(dto.getDurationMinutes());
        visit.setClientName(dto.getClientName());
        visit.setClientPhone(dto.getClientPhone());
        visit.setClientEmail(dto.getClientEmail());
        visit.setNotes(dto.getNotes());
        visit.setStatus(dto.getStatus());
        visit.setConfirmedByAgent(dto.getConfirmedByAgent());
        visit.setConfirmedByClient(dto.getConfirmedByClient());

        Visit updatedVisit = visitRepository.save(visit);
        return ResponseEntity.ok(updatedVisit);
    }

    // ✅ DELETE VISIT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        if (!visitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        visitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
