package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.common.BaseContext;
import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.dto.CreateParcelData;
import com.example.parcel_delivery_systembackendentry.entity.Parcel;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.entity.User;
import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import com.example.parcel_delivery_systembackendentry.service.ParcelService;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import com.example.parcel_delivery_systembackendentry.service.UserService;
import com.example.parcel_delivery_systembackendentry.utils.ParcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                Parcel parcel = new Parcel(data);
                parcelService.save(parcel);

                ParcelTrack parcelTrack = new ParcelTrack(
                        parcel.getId(),
                        "Estate Service created parcel label",
                        Math.toIntExact(BaseContext.getCurrentId()));
                parcelTrackService.save(parcelTrack);
                return Result.ok();
            } else {
                return Result.error(ResultCodeEnum.STUDENT_NOT_EXIST);
            }
        } else {
            return Result.error(ResultCodeEnum.ParcelInfoIncomplete);
        }
    }
}
