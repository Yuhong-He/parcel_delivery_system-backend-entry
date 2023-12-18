package com.example.parcel_delivery_systembackendentry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.parcel_delivery_systembackendentry.entity.EmailVerification;

public interface EmailVerificationService extends IService<EmailVerification> {

    // Create
    int create(EmailVerification emailVerification);

    // Read
    EmailVerification read(String email);

    // Delete
    int delete(Integer id);
}
