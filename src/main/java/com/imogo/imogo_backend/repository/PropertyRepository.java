package com.imogo.imogo_backend.repository;

import com.imogo.imogo_backend.model.ImobProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<ImobProperty, Long>, JpaSpecificationExecutor<ImobProperty> {
}
