package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.mybatis.User;
import com.example.parcel_delivery_systembackendentry.mybatis.UserService;
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
