package com.example.parcel_delivery_systembackendentry.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@Tag(name = "Parcel", description = "Parcel data controller")
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private ParcelTrackService parcelTrackService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/create")
    public Result<Object> create(@RequestBody CreateParcelData data) {
        if (ParcelUtils.validateParcelCreate(data)) {
            User u = userService.getStudentById(data.getStudent());
            if (u != null) {
                String desc = "Estate Service created parcel label";
                Parcel parcel = new Parcel(data, desc);
                parcelService.save(parcel);

                ParcelTrack parcelTrack = new ParcelTrack(
                        parcel.getId(), desc, Math.toIntExact(BaseContext.getCurrentId()));
                parcelTrackService.save(parcelTrack);
                return Result.ok();
            } else {
                return Result.error(ResultCodeEnum.STUDENT_NOT_EXIST);
            }
        } else {
            return Result.error(ResultCodeEnum.ParcelInfoIncomplete);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomPage.class))})
    @Operation(summary = "List parcels", description = "Allowed all type of users list the appropriate parcels based on the user rights.")
    @GetMapping("/list")
    public Result<Object> getParcelList(@Parameter(description = "Page Number") @RequestParam("page") Integer pageNo) {
        User currentUser = userService.getUserById(Math.toIntExact(BaseContext.getCurrentId()));
        if (currentUser.getType() == UserTypeEnum.EstateServiceStaff.getVal()) {
            Page<Parcel> page = new Page<>(pageNo, 10);
            IPage<Parcel> pageRs = parcelService.getCategoryByNameLike(page);
            List<Parcel> list = pageRs.getRecords();
            List<ParcelWithStudentInfo> newList = new ArrayList<>();
            for (Parcel p : list) {
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
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }
}
