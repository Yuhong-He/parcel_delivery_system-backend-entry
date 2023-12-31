package com.example.database_system.controller;

import com.example.database_system.mybatis.User;
import com.example.database_system.mybatis.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/getStudentsById")
    public User getStudentsById(@RequestParam int id) {
        return userService.getStudentById(id);
    }
}
