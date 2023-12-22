package com.example.entry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entry.entity.EmailTimer;

public interface EmailTimerService extends IService<EmailTimer> {

    // Create
    int create(EmailTimer emailTimer);

    // Read
    EmailTimer read(String email, String action);

    // Delete
    int delete(Integer id);
}
