package com.example.database_system.mybatis_service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    User getStudentById(int id);
    List<User> getAllPostman();
}
