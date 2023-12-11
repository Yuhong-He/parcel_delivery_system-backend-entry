package com.example.parcel_delivery_systembackendentry.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {

    @GetMapping("/")
    public String login() {
        return "Hello world";
    }

}
