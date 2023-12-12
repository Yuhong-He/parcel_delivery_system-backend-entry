package com.example.parcel_delivery_systembackendentry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.parcel_delivery_systembackendentry.entity.User;
import com.example.parcel_delivery_systembackendentry.mapper.UserMapper;
import com.example.parcel_delivery_systembackendentry.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        return baseMapper.selectOne(queryWrapper);
    }
}
