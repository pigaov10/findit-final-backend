package com.imogo.imogo_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class OllamaService {

    private final RestTemplate restTemplate;
    private final String ollamaUrl = "http://localhost:11434/api/embed";

    public OllamaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float[] generateEmbedding(String input) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format("{\"model\":\"nomic-embed-text\",\"input\":\"%s\"}", input);
        System.out.println("Request enviado ao Ollama:\n" + requestBody);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(ollamaUrl, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao gerar embedding no Ollama: " + response.getStatusCode() + " - " + response.getBody());
            }

            System.out.println("Resposta do Ollama:\n" + response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode embeddingsNode = root.path("embeddings");

            if (embeddingsNode.isMissingNode() || !embeddingsNode.isArray() || embeddingsNode.size() == 0) {
                throw new RuntimeException("Formato de embedding inválido na resposta do Ollama");
            }

            JsonNode firstEmbedding = embeddingsNode.get(0);
            if (!firstEmbedding.isArray()) {
                throw new RuntimeException("Formato do primeiro embedding inválido");
            }

            float[] embedding = new float[firstEmbedding.size()];
            for (int i = 0; i < firstEmbedding.size(); i++) {
                embedding[i] = firstEmbedding.get(i).floatValue();
            }

            System.out.println("Tamanho do embedding extraído: " + embedding.length);
            return embedding;
        } catch (Exception e) {
            System.err.println("Erro ao gerar embedding: " + e.getMessage());
            throw e;
        }
    }
}
