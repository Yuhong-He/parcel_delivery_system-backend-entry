package com.example.parcel_delivery_systembackendentry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParcelMapper extends BaseMapper<Parcel> {
}
