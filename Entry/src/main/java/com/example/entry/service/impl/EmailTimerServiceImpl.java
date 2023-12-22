package com.example.entry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entry.entity.EmailTimer;
import com.example.entry.mapper.EmailTimerMapper;
import com.example.entry.service.EmailTimerService;
import org.springframework.stereotype.Service;

@Service("emailTimerServiceImpl")
public class EmailTimerServiceImpl extends ServiceImpl<EmailTimerMapper, EmailTimer> implements EmailTimerService {

    @Override
    public int create(EmailTimer emailTimer) {
        return baseMapper.insert(emailTimer);
    }

    @Override
    public EmailTimer read(String email, String action) {
        QueryWrapper<EmailTimer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("action", action);
        queryWrapper.select().orderByDesc("timestamp");
        queryWrapper.last("limit 1");
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public int delete(Integer id) {
        return baseMapper.deleteById(id);
    }
}
