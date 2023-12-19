package com.example.parcel_delivery_systembackendentry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.mapper.ParcelTrackMapper;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import org.springframework.stereotype.Service;

@Service("parcelTrackServiceImpl")
public class ParcelTrackServiceImpl extends ServiceImpl<ParcelTrackMapper, ParcelTrack> implements ParcelTrackService {
}
