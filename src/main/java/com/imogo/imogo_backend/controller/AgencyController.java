package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.model.Agency;
import com.imogo.imogo_backend.repository.AgencyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@RequiredArgsConstructor
public class AgencyController {
    private final AgencyRepository agencyRepository;

    @PostMapping("/")
    public ResponseEntity<Agency> createAgency(@Valid @RequestBody Agency agencyRequestDTO) {
        Agency agency = new Agency();
        agency.setName(agencyRequestDTO.getName());
        agency.setEmail(agencyRequestDTO.getEmail());
        agency.setPhone(agencyRequestDTO.getPhone());
        agency.setTaxId(agencyRequestDTO.getTaxId());
        agency.setAddress(agencyRequestDTO.getAddress());
        agency.setCity(agencyRequestDTO.getCity());
        agency.setDistrict(agencyRequestDTO.getDistrict());
        agency.setPostalCode(agencyRequestDTO.getPostalCode());
        agency.setWebsite(agencyRequestDTO.getWebsite());
        agency.setContactPerson(agencyRequestDTO.getContactPerson());
        agency.setNotes(agencyRequestDTO.getNotes());
        agency.setStatus(agencyRequestDTO.getStatus());
        Agency savedAgency = agencyRepository.save(agency);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAgency);
    }

    @GetMapping("/")
    public ResponseEntity<List<Agency>> listAgencies() {
        return ResponseEntity.ok().body(agencyRepository.findAll());
    }

    // ✅ GET específico
    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgencyById(@PathVariable Long id) {
        return agencyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ PUT (Update)
    @PutMapping("/{id}")
    public ResponseEntity<Agency> updateAgency(@PathVariable Long id, @Valid @RequestBody Agency updatedAgency) {
        return agencyRepository.findById(id)
                .map(agency -> {
                    agency.setName(updatedAgency.getName());
                    agency.setEmail(updatedAgency.getEmail());
                    agency.setPhone(updatedAgency.getPhone());
                    agency.setTaxId(updatedAgency.getTaxId());
                    agency.setAddress(updatedAgency.getAddress());
                    agency.setCity(updatedAgency.getCity());
                    agency.setDistrict(updatedAgency.getDistrict());
                    agency.setPostalCode(updatedAgency.getPostalCode());
                    agency.setWebsite(updatedAgency.getWebsite());
                    agency.setContactPerson(updatedAgency.getContactPerson());
                    agency.setNotes(updatedAgency.getNotes());
                    agency.setStatus(updatedAgency.getStatus());
                    Agency saved = agencyRepository.save(agency);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Long id) {
        return agencyRepository.findById(id)
                .map(agency -> {
                    agencyRepository.delete(agency);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}