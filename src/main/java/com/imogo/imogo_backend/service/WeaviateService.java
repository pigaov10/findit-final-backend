package com.imogo.imogo_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WeaviateService {

    private final RestTemplate restTemplate;
    private final String weaviateUrl = "http://localhost:8080/v1";

    public WeaviateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createClass() throws Exception {
        String url = weaviateUrl + "/schema";

        Map<String, Object> classConfig = new HashMap<>();
        classConfig.put("class", "Property");
        classConfig.put("vectorizer", "none");

        List<Map<String, Object>> properties = List.of(
                Map.of("name", "titulo", "dataType", List.of("string")),
                Map.of("name", "descricao", "dataType", List.of("string")),
                Map.of("name", "keywords", "dataType", List.of("string[]")),
                Map.of("name", "cidade", "dataType", List.of("string")),
                Map.of("name", "bairro", "dataType", List.of("string")),
                Map.of("name", "preco", "dataType", List.of("int")),
                Map.of("name", "quartos", "dataType", List.of("int")),
                Map.of("name", "banheiros", "dataType", List.of("int")),
                Map.of("name", "area", "dataType", List.of("int"))
        );
        classConfig.put("properties", properties);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(classConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao criar classe: " + response.getBody());
        }
        return response.getBody();
    }

    public String insertProperty(String id, String titulo, String descricao, List<String> keywords,
                                 String cidade, String bairro, int preco, int quartos,
                                 int banheiros, int area, float[] vector) throws Exception {

        String url = weaviateUrl + "/objects";

        if (vector.length != 768) throw new IllegalArgumentException("Vetor deve ter dimensão 768");

        Map<String, Object> properties = new HashMap<>();
        properties.put("titulo", titulo);
        properties.put("descricao", descricao);
        properties.put("keywords", keywords);
        properties.put("cidade", cidade);
        properties.put("bairro", bairro);
        properties.put("preco", preco);
        properties.put("quartos", quartos);
        properties.put("banheiros", banheiros);
        properties.put("area", area);

        List<Float> vectorList = new ArrayList<>();
        for (float f : vector) {
            vectorList.add(f);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("class", "Property");
        body.put("id", id);
        body.put("vector", vectorList);
        body.put("properties", properties);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao inserir propriedade: " + response.getBody());
        }
        return response.getBody();
    }

    public String insertVectorOnly(String id, float[] vector) throws Exception {
        String url = weaviateUrl + "/objects";

        if (vector.length != 768) throw new IllegalArgumentException("Vetor deve ter dimensão 768");

        List<Float> vectorList = new ArrayList<>();
        for (float f : vector) {
            vectorList.add(f);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("class", "Properties");
        body.put("id", id);
        body.put("vector", vectorList);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao inserir vetor: " + response.getBody());
        }
        return response.getBody();
    }

    public Map<String, Object> searchProperties(float[] queryVector, int limit, double certainty) throws Exception {
        String url = weaviateUrl + "/graphql";

        List<Float> vectorList = new ArrayList<>();
        for (float f : queryVector) {
            vectorList.add(f);
        }
        String vectorStr = vectorList.toString();

        String graphqlQuery = """
    {
      Get {
        Properties(nearVector: {vector: %s, certainty: %s}, limit: %d) {
          _additional {
            id
            certainty
          }
        }
      }
    }
    """.formatted(vectorStr, certainty, limit);

        Map<String, Object> body = Map.of("query", graphqlQuery);
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro na busca: " + response.getBody());
        }
        return response.getBody();
    }

}
