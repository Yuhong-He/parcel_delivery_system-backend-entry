package com.example.parcel_delivery_systembackendentry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.mapper.ParcelMapper;
import com.example.parcel_delivery_systembackendentry.service.ParcelService;
import org.springframework.stereotype.Service;

@Service("parcelServiceImpl")
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements ParcelService {
}
