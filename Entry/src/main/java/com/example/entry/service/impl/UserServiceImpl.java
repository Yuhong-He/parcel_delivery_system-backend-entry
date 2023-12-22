package com.example.entry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entry.entity.User;
import com.example.entry.enumeration.UserTypeEnum;
import com.example.entry.mapper.UserMapper;
import com.example.entry.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void register(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        baseMapper.insert(user);
    }

    @Override
    public User login(String email, String password, int type) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = this.getUserByEmail(email, type);
        if(user != null && passwordEncoder.matches(password, user.getPassword())) {
            return this.getUserByEmail(email, type);
        } else {
            return null;
        }
    }

    @Override
    public User getUserById(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByEmail(String email, int type) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("type", type);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<User> getStudentsBySearchName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", name);
        queryWrapper.eq("type", UserTypeEnum.Student.getVal());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public User getStudentById(int id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("type", UserTypeEnum.Student.getVal());
        return baseMapper.selectOne(queryWrapper);
    }
}
