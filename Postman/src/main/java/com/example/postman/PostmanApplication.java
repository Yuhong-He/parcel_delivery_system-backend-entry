package com.example.postman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class PostmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostmanApplication.class, args);
    }

}
