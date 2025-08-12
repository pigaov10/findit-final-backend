package com.imogo.imogo_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.imogo.imogo_backend.service.OllamaService;
import com.imogo.imogo_backend.service.WeaviateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ia")
public class OllamaController {

    private final OllamaService ollamaService;
    private final WeaviateService weaviateService;

    public OllamaController(OllamaService ollamaService, WeaviateService weaviateService) {
        this.ollamaService = ollamaService;
        this.weaviateService = weaviateService;
    }

    // Função para garantir vetor com 768 floats
    private float[] normalizeEmbedding(float[] embedding) {
        int targetLength = 768;
        float[] normalized = new float[targetLength];
        for (int i = 0; i < targetLength; i++) {
            if (embedding != null && i < embedding.length) {
                normalized[i] = embedding[i];
            } else {
                normalized[i] = 0.0f;  // padding com zero
            }
        }
        return normalized;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarImovel(@RequestBody Map<String, Object> imovelData) {
        try {
            String descricao = (String) imovelData.get("descricao");
            String titulo = (String) imovelData.get("titulo");
            String cidade = (String) imovelData.get("cidade");
            String bairro = (String) imovelData.get("bairro");
            int preco = (int) imovelData.get("preco");
            int quartos = (int) imovelData.get("quartos");
            int banheiros = (int) imovelData.get("banheiros");
            int area = (int) imovelData.get("area");
            @SuppressWarnings("unchecked")
            List<String> keywords = (List<String>) imovelData.get("keywords");

            // Gerar embedding
            float[] embedding = ollamaService.generateEmbedding(descricao);

            // Normalizar para 768 floats
            float[] normalizedEmbedding = normalizeEmbedding(embedding);

            String id = UUID.randomUUID().toString();

            String response = weaviateService.insertProperty(
                    id,
                    titulo,
                    descricao,
                    keywords,
                    cidade,
                    bairro,
                    preco,
                    quartos,
                    banheiros,
                    area,
                    normalizedEmbedding
            );

            return ResponseEntity.ok(Map.of("id", id, "message", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchProperty(@RequestBody Map<String, Object> requestData) {
        try {
            String query = (String) requestData.get("query");
            int limit = (int) requestData.getOrDefault("limit", 10);
            double certainty = Double.parseDouble(requestData.getOrDefault("certainty", 0.85).toString());

            float[] queryEmbedding = ollamaService.generateEmbedding(query);
            float[] normalizedQueryEmbedding = normalizeEmbedding(queryEmbedding);

            Map<String, Object> searchResult = weaviateService.searchProperties(normalizedQueryEmbedding, limit, certainty);

            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
