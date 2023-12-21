package com.example.entry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entry.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
