package com.imogo.imogo_backend.repository;

import com.imogo.imogo_backend.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
