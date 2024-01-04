package com.example.database_system.controller;

import com.example.database_system.mybatis_service.User;
import com.example.database_system.mybatis_service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/getStudentById")
    public User getStudentsById(@RequestParam int id) {
        return userService.getStudentById(id);
    }
}
