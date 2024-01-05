package com.example.database_system.controller;

import com.example.database_system.mybatis_service.User;
import com.example.database_system.mybatis_service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/getStudentById")
    public User getStudentsById(@RequestParam int id) {
        return userService.getStudentById(id);
    }

    @GetMapping("/getPostmanIds")
    public List<Integer> getPostmanIds() {
        List<User> postmanList = userService.getAllPostman();
        List<Integer> idList = new ArrayList<>();
        for (User user: postmanList) {
            idList.add(user.getId());
        }
        return idList;
    }

    @GetMapping("/getPostmanEmail")
    public String getPostmanEmail(@RequestParam int id) {
        User postman = userService.getById(id);
        return postman.getEmail();
    }
}
