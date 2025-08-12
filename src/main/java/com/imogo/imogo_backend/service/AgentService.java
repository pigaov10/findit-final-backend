package com.imogo.imogo_backend.service;

import com.imogo.imogo_backend.dto.property_agent.PropertyAgentRequestDTO;
import com.imogo.imogo_backend.model.Agency;
import com.imogo.imogo_backend.model.Agent;
import com.imogo.imogo_backend.repository.AgencyRepository;
import com.imogo.imogo_backend.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    private final AgentRepository agentRepository;
    private final AgencyRepository agencyRepository;

    public AgentService(AgentRepository agentRepository, AgencyRepository agencyRepository) {
        this.agentRepository = agentRepository;
        this.agencyRepository = agencyRepository;
    }

    public Agent createAgent(PropertyAgentRequestDTO dto) {
        Agent agent = new Agent();
        agent.setName(dto.getName());
        agent.setEmail(dto.getEmail());
        agent.setPhone(dto.getPhone());

        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + dto.getAgencyId()));

        agent.setAgency(agency);

        return agentRepository.save(agent);
    }

    public List<Agent> findAllAgents() {
        return agentRepository.findAll();
    }
}
