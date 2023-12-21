package com.example.email.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UCD Parcel Delivery System - Email Server API")
                        .description("This server contains only the send email service. And it can only be invoked by other UCD Parcel Delivery Systems. Data transmission between other services and this service is encrypted, and this service alone cannot be unit tested.")
                        .version("v1.0.0")
                );
    }
}