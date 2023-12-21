package com.example.entry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entry.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    // Create
    void register(User user);

    // Read
    User login(String email, String password, int type);
    User getUserById(Integer id);
    User getUserByEmail(String email, int type);
    List<User> getStudentsBySearchName(String name);
    User getStudentById(int id);

}
