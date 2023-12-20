package com.example.entry.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entry.common.BaseContext;
import com.example.entry.common.Result;
import com.example.entry.dto.CreateParcelData;
import com.example.entry.dto.CustomPage;
import com.example.entry.dto.ParcelWithStudentInfo;
import com.example.entry.dto.StudentInfo;
import com.example.entry.entity.Parcel;
import com.example.entry.entity.User;
import com.example.entry.enumeration.ResultCodeEnum;
import com.example.entry.enumeration.UserTypeEnum;
import com.example.entry.service.ParcelService;
import com.example.entry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Parcel", description = "Parcel data controller")
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @Autowired
    private UserService userService;

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/create")
    public Result<Object> create(@RequestBody CreateParcelData data) {
        RestTemplate template = new RestTemplate();
        template.postForEntity("http://localhost:18082/estate/createParcel?staff=" + BaseContext.getCurrentId().intValue(), data, String.class);
        return Result.ok();
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
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }
}
