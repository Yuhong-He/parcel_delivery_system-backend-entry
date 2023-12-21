package com.example.estate.controller;

import com.example.estate.dto.*;
import com.example.estate.entity.Parcel;
import com.example.estate.entity.ParcelTrack;
import com.example.estate.entity.User;
import com.example.estate.utils.ParcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.web.bind.annotation.*;
import com.example.estate.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@Tag(name = "Estate", description = "Estate controller")
@RequestMapping("/")
public class EstateController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Estate Service Staff System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Estate'>GitHub page</a>.</h2>";
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/estate/createParcel")
    public void createParcel(@RequestBody ParcelInfo data,
                             @Parameter(description = "Estate Service Staff Account ID") @RequestParam("staff") int staff) {
        if (ParcelUtils.validateParcelCreate(data)) {
            User u = userService.getStudentById(data.getStudent());
            if (u != null) {
                String desc = "Estate Service created parcel label";
                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                ParcelTrack parcelTrack = new ParcelTrack(desc, staff, formattedDateTime);
                Parcel parcel = new Parcel(data, List.of(parcelTrack));
                mongoTemplate.save(parcel);
            } else {
                log.error("Student " + data.getStudent() + " not exist.");
            }
        } else {
            log.error("Parcel info incomplete: " + data);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomPage.class))})
    @Operation(summary = "List parcels", description = "Allowed Estate Service Staff list parcels.")
    @GetMapping("/estate/list")
    public CustomPage getParcelList(@Parameter(description = "Page Number") @RequestParam("page") Integer pageNo) {
        int size = 10;
        AggregationOperation unwind = Aggregation.unwind("tracks");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
        AggregationOperation group = Aggregation.group("_id")
                .first("type").as("type")
                .first("address1").as("address1")
                .first("address2").as("address2")
                .first("student").as("student")
                .first("tracks").as("latestTrack");
        AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "latestTrack.create_at");
        Aggregation aggregation = Aggregation.newAggregation(unwind, sort, group, secondSort);
        AggregationResults<ParcelWithLatestTrack> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelWithLatestTrack.class);
        List<ParcelWithLatestTrack> parcels = results.getMappedResults();
        int total = parcels.size();
        int fromIndex = Math.min((pageNo - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<ParcelWithLatestTrack> list = parcels.subList(fromIndex, toIndex);
        List<ParcelWithStudentInfo> newList = new ArrayList<>();
        for(ParcelWithLatestTrack p : list) {
            User student = userService.getStudentById(p.getStudent());
            ParcelTrack latestTrack = p.getLatestTrack();
            ParcelWithStudentInfo parcelWithStudentInfo = new ParcelWithStudentInfo(
                    p.getId(), new StudentInfo(student.getUsername(), student.getEmail()),
                    p.getType(), p.getAddress1(), p.getAddress2(), latestTrack.getDescription(), latestTrack.getCreate_at());
            newList.add(parcelWithStudentInfo);
        }
        long pages = (long) Math.ceil((double) total / size);
        return new CustomPage(newList, total, size, pageNo, pages);
    }

}
