package com.example.parcel_delivery_systembackendentry.mybatis;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.parcel_delivery_systembackendentry.mybatis.User;

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
