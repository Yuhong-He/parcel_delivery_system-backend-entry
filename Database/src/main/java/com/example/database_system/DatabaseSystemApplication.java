package com.example.database_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ServletComponentScan
@EnableMongoRepositories("com.example.database_system.MongoDB")
public class DatabaseSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseSystemApplication.class, args);
    }

}
