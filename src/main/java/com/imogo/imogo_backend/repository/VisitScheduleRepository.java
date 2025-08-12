package com.imogo.imogo_backend.repository;

import com.imogo.imogo_backend.model.VisitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitScheduleRepository extends JpaRepository<VisitSchedule, Long> {
}
