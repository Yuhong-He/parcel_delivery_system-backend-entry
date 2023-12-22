package com.example.estate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.estate.entity.User;

public interface UserService extends IService<User> {
    User getStudentById(int id);
}
