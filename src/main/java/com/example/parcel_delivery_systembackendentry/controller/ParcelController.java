package com.example.parcel_delivery_systembackendentry.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.parcel_delivery_systembackendentry.common.BaseContext;
import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.dto.CreateParcelData;
import com.example.parcel_delivery_systembackendentry.dto.CustomPage;
import com.example.parcel_delivery_systembackendentry.dto.ParcelWithStudentInfo;
import com.example.parcel_delivery_systembackendentry.dto.StudentInfo;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.entity.User;
import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import com.example.parcel_delivery_systembackendentry.enumeration.UserTypeEnum;
import com.example.parcel_delivery_systembackendentry.service.ParcelService;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import com.example.parcel_delivery_systembackendentry.service.UserService;
import com.example.parcel_delivery_systembackendentry.utils.ParcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private ParcelTrackService parcelTrackService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Result<Object> create(@RequestBody CreateParcelData data) {
        if (ParcelUtils.validateParcelCreate(data)) {
            User u = userService.getStudentById(data.getStudent());
            if (u != null) {
                String desc = "Estate Service created parcel label";
                Parcel parcel = new Parcel(data, desc);
                parcelService.save(parcel);

                ParcelTrack parcelTrack = new ParcelTrack(
                        parcel.getId(), desc, Math.toIntExact(BaseContext.getCurrentId())
                );
                parcelTrackService.save(parcelTrack);
                return Result.ok();
            } else {
                return Result.error(ResultCodeEnum.STUDENT_NOT_EXIST);
            }
        } else {
            return Result.error(ResultCodeEnum.ParcelInfoIncomplete);
        }
    }

    @GetMapping("/list")
    public Result<Object> getParcelList(@RequestParam("page") Integer pageNo) {
        User currentUser = userService.getUserById(Math.toIntExact(BaseContext.getCurrentId()));
        if (currentUser.getType() == UserTypeEnum.EstateServiceStaff.getType()) {
            Page<Parcel> page = new Page<>(pageNo, 10);
            IPage<Parcel> pageRs =  parcelService.getCategoryByNameLike(page);
            List<Parcel> list = pageRs.getRecords();
            List<ParcelWithStudentInfo> newList = new ArrayList<>();
            for(Parcel p : list) {
                User student = userService.getStudentById(p.getStudent());
                ParcelWithStudentInfo parcelWithStudentInfo = new ParcelWithStudentInfo(
                        p.getId(), new StudentInfo(student.getUsername(), student.getEmail()),
                        p.getType(), p.getAddress1(), p.getAddress2(), p.getLastUpdateDesc(), p.getLastUpdateAt());
                newList.add(parcelWithStudentInfo);
            }
            CustomPage customPage = new CustomPage(newList, pageRs.getTotal(), pageRs.getSize(),
                    pageRs.getCurrent(), pageRs.getPages());
            return Result.ok(customPage);
        } else {
            System.out.println("aaa");
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }
}
