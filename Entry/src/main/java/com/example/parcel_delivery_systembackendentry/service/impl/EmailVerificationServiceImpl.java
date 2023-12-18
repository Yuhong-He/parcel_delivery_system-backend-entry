package com.example.parcel_delivery_systembackendentry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.parcel_delivery_systembackendentry.entity.EmailVerification;
import com.example.parcel_delivery_systembackendentry.mapper.EmailVerificationMapper;
import com.example.parcel_delivery_systembackendentry.service.EmailVerificationService;
import org.springframework.stereotype.Service;

@Service("emailVerificationServiceImpl")
public class EmailVerificationServiceImpl extends ServiceImpl<EmailVerificationMapper, EmailVerification> implements EmailVerificationService {

    @Override
    public int create(EmailVerification emailVerification) {
        return baseMapper.insert(emailVerification);
    }

    @Override
    public EmailVerification read(String email) {
        QueryWrapper<EmailVerification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.select().orderByDesc("timestamp");
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public int delete(Integer id) {
        return baseMapper.deleteById(id);
    }

}
