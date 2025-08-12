package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.service.EuriborScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/euribor/tax")
public class EuriborController {
    private final EuriborScraperService euriborScraperService;

    public EuriborController(EuriborScraperService euriborScraperService) {
        this.euriborScraperService = euriborScraperService;
    }

    @GetMapping("/")
    public Double getEuribor() {
        return euriborScraperService.fetchEuriborRate();
    }

    record EuriborResponse(double rate, java.time.LocalDate updatedAt) {}
}
