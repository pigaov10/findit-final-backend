package com.imogo.imogo_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Real Estate Application - IMOGO",
                version = "1.0",
                description = "API for managing sample entities"
        )
)
public class SwaggerConfig {
}
