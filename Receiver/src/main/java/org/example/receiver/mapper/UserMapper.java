package org.example.receiver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.receiver.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
