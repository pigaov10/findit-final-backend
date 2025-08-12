package com.imogo.imogo_backend.dto.ollama;

import lombok.Data;


@Data
public class OllamaResponse {
    private String model;
    private String response;
    private boolean done;
    private String done_reason;
}
