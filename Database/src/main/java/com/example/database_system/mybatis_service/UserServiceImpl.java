package com.example.database_system.mybatis_service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.database_system.enumeration.UserTypeEnum;
import com.example.database_system.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getStudentById(int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("type", UserTypeEnum.Student.getVal());
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<User> getAllPostman() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", UserTypeEnum.Postman.getVal());
        return baseMapper.selectList(queryWrapper);
    }
}
