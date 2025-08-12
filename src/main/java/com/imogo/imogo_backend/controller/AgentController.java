package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.Agency;
import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.repository.AgencyRepository;
import com.imogo.imogo_backend.repository.AgentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentRepository agentRepository;
    private final AgencyRepository agencyRepository;

    // CREATE
    @PostMapping("/")
    public ResponseEntity<?> createAgent(@Valid @RequestBody AgentAgencyDTO agentDTO) {
        Optional<Agency> agency = agencyRepository.findById(agentDTO.getAgency());
        if (agency.isEmpty()) {
            return ResponseEntity.badRequest().body("Agência não encontrada.");
        }

        Agent agent = new Agent();
        agent.setName(agentDTO.getName());
        agent.setLicenseNumber(agentDTO.getLicenseNumber());
        agent.setEmail(agentDTO.getEmail());
        agent.setPhone(agentDTO.getPhone());
        agent.setTaxId(agentDTO.getTaxId());
        agent.setAddress(agentDTO.getAddress());
        agent.setCity(agentDTO.getCity());
        agent.setDistrict(agentDTO.getDistrict());
        agent.setPostalCode(agentDTO.getPostalCode());
        agent.setWebsite(agentDTO.getWebsite());
        agent.setContactPerson(agentDTO.getContactPerson());
        agent.setNotes(agentDTO.getNotes());
        agent.setStatus(agentDTO.getStatus());
        agent.setAgency(agency.get());

        Agent savedAgent = agentRepository.save(agent);
        return ResponseEntity.status(201).body(savedAgent);
    }

    // READ ALL
    @GetMapping("/")
    public ResponseEntity<List<AgentDTO>> listAgents() {
        List<AgentDTO> agentsDto = agentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agentsDto);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getAgentById(@PathVariable Long id) {
        return agentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable Long id, @Valid @RequestBody Agent updatedAgent) {
        Optional<Agent> existingOpt = agentRepository.findById(id);
        if (existingOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Optional<Agency> agencyOpt = agencyRepository.findById(updatedAgent.getAgency().getId());
        if (agencyOpt.isEmpty())
            return ResponseEntity.badRequest().body("Agência não encontrada.");

        Agent existing = existingOpt.get();

        existing.setName(updatedAgent.getName());
        existing.setEmail(updatedAgent.getEmail());
        existing.setPhone(updatedAgent.getPhone());
        existing.setLicenseNumber(updatedAgent.getLicenseNumber());
        existing.setTaxId(updatedAgent.getTaxId());
        existing.setAddress(updatedAgent.getAddress());
        existing.setCity(updatedAgent.getCity());
        existing.setDistrict(updatedAgent.getDistrict());
        existing.setPostalCode(updatedAgent.getPostalCode());
        existing.setWebsite(updatedAgent.getWebsite());
        existing.setContactPerson(updatedAgent.getContactPerson());
        existing.setNotes(updatedAgent.getNotes());
        existing.setStatus(updatedAgent.getStatus());
        existing.setAgency(agencyOpt.get());

        Agent saved = agentRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        if (!agentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        agentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper para converter Agent -> AgentDTO
    private AgentDTO convertToDto(Agent agent) {
        AgentDTO dto = new AgentDTO();
        dto.setId(agent.getId());
        dto.setName(agent.getName());
        dto.setLicenseNumber(agent.getLicenseNumber());
        dto.setEmail(agent.getEmail());
        dto.setPhone(agent.getPhone());
        dto.setTaxId(agent.getTaxId());
        dto.setAddress(agent.getAddress());
        dto.setCity(agent.getCity());
        dto.setDistrict(agent.getDistrict());
        dto.setPostalCode(agent.getPostalCode());
        dto.setWebsite(agent.getWebsite());
        dto.setContactPerson(agent.getContactPerson());
        dto.setNotes(agent.getNotes());
        dto.setStatus(agent.getStatus());
        AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(agent.getAgency().getId());
        agencyDTO.setName(agent.getAgency().getName());
        dto.setAgency(agencyDTO);
        return dto;
    }

    // Helper para converter AgentDTO -> Agent (para criação ou atualização)
    private Agent convertToEntity(AgentDTO dto, Agency agency) {
        Agent agent = new Agent();
        agent.setName(dto.getName());
        agent.setLicenseNumber(dto.getLicenseNumber());
        agent.setEmail(dto.getEmail());
        agent.setPhone(dto.getPhone());
        agent.setTaxId(dto.getTaxId());
        agent.setAddress(dto.getAddress());
        agent.setCity(dto.getCity());
        agent.setDistrict(dto.getDistrict());
        agent.setPostalCode(dto.getPostalCode());
        agent.setWebsite(dto.getWebsite());
        agent.setContactPerson(dto.getContactPerson());
        agent.setNotes(dto.getNotes());
        agent.setStatus(dto.getStatus());
        agent.setAgency(agency);
        return agent;
    }
}
