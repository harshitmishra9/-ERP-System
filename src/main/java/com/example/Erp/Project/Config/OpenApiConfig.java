package com.example.Erp.Project.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI erpOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ERP Management System API")
                        .description("Inventory, Sales, Purchase, Invoice APIs")
                        .version("1.0"));
    }
}
