package com.imogo.imogo_backend.dto.ollama;

import lombok.Data;


@Data
public class OllamaRequest {
    private String model;
    private String prompt;
    private int max_tokens;
    private boolean stream;
}
