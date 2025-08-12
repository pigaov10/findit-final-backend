package com.imogo.imogo_backend.service;

import com.imogo.imogo_backend.dto.PropertySearchDTO;
import com.imogo.imogo_backend.model.City;
import com.imogo.imogo_backend.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NaturalLanguageParserService {

    private final CityRepository cityRepository;

    public NaturalLanguageParserService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public PropertySearchDTO parse(String phrase) {
        phrase = phrase.toLowerCase();
        PropertySearchDTO dto = new PropertySearchDTO();

        if (phrase.contains("casa")) dto.setType("casa");
        else if (phrase.contains("apartamento")) dto.setType("apartamento");

        if (phrase.contains("vender") || phrase.contains("venda")) dto.setPurpose("venda");
        else if (phrase.contains("alugar") || phrase.contains("aluguel")) dto.setPurpose("aluguel");

        // Busca cidades do banco
        List<City> cities = cityRepository.findAll();

        for (City city : cities) {
            if (phrase.contains(city.getName().toLowerCase())) {
                dto.setCity(city.getName());
                break;
            }
        }

        Pattern quartosPattern = Pattern.compile("(\\d+) quartos?");
        Matcher matcher = quartosPattern.matcher(phrase);
        if (matcher.find()) {
            dto.setBedrooms(Integer.parseInt(matcher.group(1)));
        }

        System.out.println("🔍 Parsed DTO: " + dto);
        return dto;
    }
}
