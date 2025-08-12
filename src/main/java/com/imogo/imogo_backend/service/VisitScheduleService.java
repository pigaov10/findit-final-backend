package com.imogo.imogo_backend.service;

import com.imogo.imogo_backend.model.VisitSchedule;
import com.imogo.imogo_backend.repository.VisitScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitScheduleService {
    @Autowired
    private VisitScheduleRepository visitScheduleRepository;

    public VisitSchedule createVisit(VisitSchedule visitSchedule) {
        return visitScheduleRepository.save(visitSchedule);
    }
}
