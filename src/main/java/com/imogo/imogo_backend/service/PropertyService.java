package com.imogo.imogo_backend.service;

import com.imogo.imogo_backend.controller.VectorResponse;
import com.imogo.imogo_backend.dto.property.ImobPropertyRequestDTO;
import com.imogo.imogo_backend.dto.property.ImobPropertyResponseDTO;
import com.imogo.imogo_backend.events.PropertyEvent;
import com.imogo.imogo_backend.mapper.ImobPropertyMapper;
import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.model.ImobProperty;
import com.imogo.imogo_backend.model.enums.EventType;
import com.imogo.imogo_backend.repository.AgentRepository;
import com.imogo.imogo_backend.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private RestTemplate restTemplate;

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

        property.setWeaviateId(UUID.randomUUID().toString());
        ImobProperty savedProperty = propertyRepository.save(property);
        if (savedProperty.getId() == null) {
            throw new IllegalStateException(PROPERTY_NOT_SAVED_MSG);
        }

        PropertyEvent event = new PropertyEvent();
        event.setId(UUID.fromString(savedProperty.getWeaviateId()));
        event.setTitle(savedProperty.getTitle());
        event.setPrice(savedProperty.getPrice().doubleValue());
        event.setArea(Double.valueOf(savedProperty.getSquareFeet()));
        event.setBathrooms(savedProperty.getBathrooms());
        event.setBedrooms(savedProperty.getBedrooms());
        event.setDistrict("boavista");
        event.setCity(savedProperty.getCity());
        event.setEnergyCertificate("A");
        event.setNearby(List.of("natureza", "praia"));
        event.setFeatures(List.of("tv", "ar condicionado"));
        event.setLatitude(savedProperty.getPropertyLocation().getLatitude());
        event.setLongitude(savedProperty.getPropertyLocation().getLongitude());
        event.setFloor(2);
        event.setFurnished(true);
        event.setCondition("boa");
        event.setDescription(savedProperty.getDescription());
        event.setType(EventType.CREATED);
        event.setPropertyType("Apartamento");
        event.setYearBuilt(2020);
        rabbitTemplate.convertAndSend("property-exchange", "property.created", event);
        return imobPropertyMapper.toDTO(savedProperty);
    }

    public String getPropertiesByIA(String query) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8082/weaviate/search")
                .queryParam("q", "Apartamento T3 pequeno estilo loft sem varanda em boavista")
                .toUriString();
        VectorResponse a = restTemplate.getForObject(url, VectorResponse.class);
        List<String> price = a.getData().getGet().getProperty().stream().map(property -> property.getAdditional().getId()).collect(Collectors.toList());
        System.out.println(price);
        return "ola";
        // Faz a requisição GET
        //return restTemplate.getForObject(url, String.class);
        //String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        //String url = String.format("http://localhost:8082/weaviate/search?q=%s", encodedQuery);
        //ResponseEntity<Map> vectorR = restTemplate.getForEntity(url, Map.class);
        //System.out.println(vectorR);
        //Pageable pageable = PageRequest.of(page, size);
        //return propertyRepository.findAll(pageable).map(imobPropertyMapper::toDTO);
    }

    public Page<ImobPropertyResponseDTO> getAllProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8082/weaviate/search")
                .queryParam("q", "Apartamento T3 pequeno estilo loft sem varanda em boavista")
                .toUriString();
        VectorResponse responseVector = restTemplate.getForObject(url, VectorResponse.class);
        List<String> ids = responseVector.getData().getGet().getProperty().stream().map(property -> property.getAdditional().getId()).collect(Collectors.toList());
        System.out.println(ids);
        return propertyRepository.findByWeaviateIdIn(ids, pageable).map(imobPropertyMapper::toDTO);
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
