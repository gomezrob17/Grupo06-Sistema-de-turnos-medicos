package com.Gropo06.turnos_medicos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Turnos Médicos", version = "v1", description = "Documentación de endpoints REST", license = @License(name = "MIT")))
public class OpenApiConfig {
}
