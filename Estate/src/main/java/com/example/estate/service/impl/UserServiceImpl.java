package com.example.estate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.estate.entity.User;
import com.example.estate.enumeration.UserTypeEnum;
import com.example.estate.mapper.UserMapper;
import com.example.estate.service.UserService;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getStudentById(int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("type", UserTypeEnum.Student.getVal());
        return baseMapper.selectOne(queryWrapper);
    }
}
