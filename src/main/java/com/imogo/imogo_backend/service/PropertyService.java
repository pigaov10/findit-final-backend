package com.imogo.imogo_backend.service;

import com.imogo.imogo_backend.dto.property.ImobPropertyRequestDTO;
import com.imogo.imogo_backend.dto.property.ImobPropertyResponseDTO;
import com.imogo.imogo_backend.mapper.ImobPropertyMapper;
import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.model.ImobProperty;
import com.imogo.imogo_backend.repository.AgentRepository;
import com.imogo.imogo_backend.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private static final String AGENT_NOT_FOUND_MSG = "Agente com ID %d não encontrado";
    private static final String PROPERTY_NOT_SAVED_MSG = "Falha ao salvar o imóvel. ID nulo após persistência.";

    private final PropertyRepository propertyRepository;
    private final ImobPropertyMapper imobPropertyMapper;
    private final AgentRepository agentRepository;
    private final OllamaService ollamaService;
    private final WeaviateService weaviateService;

    @Transactional
    public ImobPropertyResponseDTO saveProperty(ImobPropertyRequestDTO imobPropertyRequestDTO) throws Exception {
        Long agentId = imobPropertyRequestDTO.getPropertyAgent().getId();
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(AGENT_NOT_FOUND_MSG, agentId)));

        ImobProperty property = imobPropertyMapper.toEntity(imobPropertyRequestDTO);
        property.setAgent(agent);

        if (property.getPropertyImages() != null) {
            property.getPropertyImages().forEach(image -> image.setImobProperty(property));
        }

        ImobProperty savedProperty = propertyRepository.save(property);
        if (savedProperty.getId() == null) {
            throw new IllegalStateException(PROPERTY_NOT_SAVED_MSG);
        }

        float[] embedding = ollamaService.generateEmbedding(savedProperty.getDescription());
        float[] normalizedEmbedding = normalizeEmbedding(embedding);
        String weaviateId = UUID.randomUUID().toString();

        try {
            weaviateService.insertVectorOnly(weaviateId, normalizedEmbedding);
            savedProperty.setWeaviateId(weaviateId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar propriedade no vetor semântico", e);
        }
        return imobPropertyMapper.toDTO(savedProperty);
    }

    public Page<ImobPropertyResponseDTO> getAllProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImobProperty> resp = propertyRepository.findAll(pageable);
        Page<ImobPropertyResponseDTO> response = propertyRepository.findAll(pageable).map(imobPropertyMapper::toDTO);
        return response;
    }

    public Optional<ImobPropertyResponseDTO> getPropertyById(Long id) {
        return propertyRepository.findById(id).map(imobPropertyMapper::toDTO);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    /*
    public Page<ImobPropertyResponseDTO> searchProperties(PropertySearchDTO dto, int page, int size) {
        Specification<PropertySearchDTO> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(page, size);

        if (dto.getType() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("type").as(String.class)), dto.getType().toLowerCase()));
        }

        if (dto.getPurpose() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("purpose").as(String.class)), dto.getPurpose().toLowerCase()));
        }

        if (dto.getCity() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("city").as(String.class)), "%" + dto.getCity().toLowerCase() + "%"));
        }

        if (dto.getBedrooms() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("bedrooms"), dto.getBedrooms()));
        }

        propertyRepository.findAll(spec, pageable);
        return imobPropertyMapper.toDTO(propertiesImo.c);
    } */

    private float[] normalizeEmbedding(float[] embedding) {
        int targetLength = 768;
        float[] normalized = new float[targetLength];
        for (int i = 0; i < targetLength; i++) {
            if (embedding != null && i < embedding.length) {
                normalized[i] = embedding[i];
            } else {
                normalized[i] = 0.0f;  // padding com zero
            }
        }
        return normalized;
    }
}
