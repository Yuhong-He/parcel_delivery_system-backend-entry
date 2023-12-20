package com.example.entry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entry.entity.ParcelTrack;
import com.example.entry.mapper.ParcelTrackMapper;
import com.example.entry.service.ParcelTrackService;
import org.springframework.stereotype.Service;

@Service("parcelTrackServiceImpl")
public class ParcelTrackServiceImpl extends ServiceImpl<ParcelTrackMapper, ParcelTrack> implements ParcelTrackService {
}
