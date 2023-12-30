package org.example.receiver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.receiver.entity.User;
import org.example.receiver.enumeration.UserTypeEnum;
import org.example.receiver.mapper.UserMapper;
import org.example.receiver.service.UserService;
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
