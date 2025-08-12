package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.dto.PropertiesDTO;
import com.imogo.imogo_backend.dto.PropertySearchDTO;
import com.imogo.imogo_backend.service.NaturalLanguageParserService;
import com.imogo.imogo_backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchPropertyController {
    @Autowired
    private NaturalLanguageParserService parserService;

    @Autowired
    private PropertyService propertyService;

}
