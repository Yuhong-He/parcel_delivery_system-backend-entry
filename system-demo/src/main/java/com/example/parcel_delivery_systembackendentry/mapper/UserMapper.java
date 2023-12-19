package com.example.parcel_delivery_systembackendentry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.parcel_delivery_systembackendentry.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
