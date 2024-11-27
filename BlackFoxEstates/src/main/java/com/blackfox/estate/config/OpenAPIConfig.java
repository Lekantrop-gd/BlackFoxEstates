package com.blackfox.estate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Black Fox Estate API", version = "1.0", description = "API for managing hotel bookings")
)
public class OpenAPIConfig {}
