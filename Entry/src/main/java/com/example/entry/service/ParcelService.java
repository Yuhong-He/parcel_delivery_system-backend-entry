package com.example.entry.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entry.entity.Parcel;

public interface ParcelService extends IService<Parcel> {
    IPage<Parcel> getCategoryByNameLike(Page<Parcel> page);
}
