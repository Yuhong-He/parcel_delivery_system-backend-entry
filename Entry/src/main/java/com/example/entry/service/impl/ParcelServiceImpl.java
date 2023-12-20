package com.example.entry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entry.entity.Parcel;
import com.example.entry.mapper.ParcelMapper;
import com.example.entry.service.ParcelService;
import org.springframework.stereotype.Service;

@Service("parcelServiceImpl")
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements ParcelService {
    @Override
    public IPage<Parcel> getCategoryByNameLike(Page<Parcel> page) {
        QueryWrapper<Parcel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "type", "address1", "address2", "student", "last_update_desc", "last_update_at");
        queryWrapper.last(" ORDER BY last_update_at DESC");
        return baseMapper.selectPage(page, queryWrapper);
    }
}
