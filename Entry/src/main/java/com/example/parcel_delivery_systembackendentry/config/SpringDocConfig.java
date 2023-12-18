package com.example.parcel_delivery_systembackendentry.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UCD Parcel Delivery System - Entry Server API")
                        .version("v1.0.0")
                ).security(List.of(new SecurityRequirement().addList("Accesstoken")));
    }
}