package com.example.entry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entry.entity.EmailVerification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailVerificationMapper extends BaseMapper<EmailVerification> {
}
