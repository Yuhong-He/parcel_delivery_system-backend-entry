package com.example.database_system;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SecurityScheme(name = "Accesstoken", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@ServletComponentScan
@EnableMongoRepositories("com.example.database_system.MongoDB")
public class DatabaseSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseSystemApplication.class, args);
    }

}
