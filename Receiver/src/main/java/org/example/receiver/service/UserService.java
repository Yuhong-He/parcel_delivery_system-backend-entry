package org.example.receiver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.receiver.entity.User;

public interface UserService extends IService<User> {
    User getStudentById(int id);
}
