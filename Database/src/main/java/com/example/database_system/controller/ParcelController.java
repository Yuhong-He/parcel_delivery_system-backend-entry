package com.example.database_system.controller;

import com.example.database_system.MongoDB.Parcel;
import com.example.database_system.MongoDB.ParcelRepository;
import com.example.database_system.dto.*;
import com.example.database_system.mybatis_service.User;
import com.example.database_system.mybatis_service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/parcel")
public class ParcelController {
    @Resource
    private ParcelRepository parcelRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userService;

    @Operation(description = "Create a new Parcel")
    @PostMapping(value = "/newParcel")
    public int newParcel(@Parameter(description = "an Parcel Object") @RequestBody Parcel parcel) {
        // implemented using mom
        parcelRepository.save(parcel);
        return 0;
    }

    @Operation(description = "update a parcelTrack")
    @PutMapping("/updateTrack")
    public int updateTrack(
            @Parameter(description = "updated ParcelTrack with Parcel ID") @RequestBody ParcelTrackWithParcelID parcelTrackWithParcelID) {
        // implemented using mom
        return 0;
    }

    @Operation(description = "get parcels of a receiver")
    @GetMapping(value = "/getReceiverParcel")
    public CustomPage getReceiverParcel(@Parameter(description = "User's ID") @RequestParam int receiverId, @RequestParam int pageNo) {
        int size = 10;
        int skip = (pageNo - 1) * size;
        SkipOperation skipOperation = skip(skip);
        LimitOperation limitOperation = limit(size);

        Criteria criteria = Criteria.where("student").is(receiverId);
        MatchOperation matchOperation = match(criteria);
        AggregationOperation unwind = Aggregation.unwind("tracks");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
        AggregationOperation group = Aggregation.group("_id")
                .first("type").as("type")
                .last("tracks.description").as("lastUpdateDesc")
                .last("tracks.create_at").as("lastUpdateAt");
        AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "lastUpdateAt");

        Aggregation aggregation = newAggregation(matchOperation, unwind, sort, group, secondSort, skipOperation, limitOperation);

        AggregationResults<ParcelDisplayForStudent> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelDisplayForStudent.class);
        List<ParcelDisplayForStudent> parcels = results.getMappedResults();

        final Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Parcel.class);
        long pages = (long) Math.ceil((double) total / size);

        return new CustomPage(parcels, (int) total, size, pageNo, pages);
    }

    @Operation(description = "Get all parcels", responses = {
            @ApiResponse(description = "a JSONised Slice<Page> object") })
    @GetMapping(value = "/getAllParcels")
    public CustomPage getAllParcel(@RequestParam int pageNo) {
        int size = 10;
        int skip = (pageNo - 1) * size;
        SkipOperation skipOperation = skip(skip);
        LimitOperation limitOperation = limit(size);

        AggregationOperation unwind = Aggregation.unwind("tracks");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
        AggregationOperation group = Aggregation.group("_id")
                .first("type").as("type")
                .first("address1").as("address1")
                .first("address2").as("address2")
                .first("student").as("student")
                .first("tracks.description").as("lastUpdateDesc")
                .first("tracks.create_at").as("lastUpdateAt");
        AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "lastUpdateAt");

        Aggregation aggregation = newAggregation(unwind, sort, group, secondSort, skipOperation, limitOperation);

        AggregationResults<ParcelDisplayForEstate> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelDisplayForEstate.class);
        List<ParcelDisplayForEstate> parcels = results.getMappedResults();

        List<ParcelWithStudentInfo> newList = new ArrayList<>();
        for(ParcelDisplayForEstate p : parcels) {
            User student = userService.getStudentById(p.getStudent());
            ParcelWithStudentInfo parcelWithStudentInfo = new ParcelWithStudentInfo(
                    p.getId(), new StudentInfo(student.getUsername(), student.getEmail()),
                    p.getType(), p.getAddress1(), p.getAddress2(), p.getLastUpdateDesc(), p.getLastUpdateAt());
            newList.add(parcelWithStudentInfo);
        }

        final Query query = new Query();
        long total = mongoTemplate.count(query, Parcel.class);
        long pages = (long) Math.ceil((double) total / size);

        return new CustomPage(newList, (int) total, size, pageNo, pages);
    }

    @Operation(description = "get a specific parcel", responses = {
            @ApiResponse(description = "a Parcel Object") })
    @GetMapping(value = "/getParcelWithId/{id}")
    public Parcel getParcelWithId(@Parameter(description = "user's ID") @PathVariable String id){
        return parcelRepository.findById(id).orElse(null);
    }
    @Operation(description = "Get all letters for a postman")
    @GetMapping(value = "/getLetters")
    public Page<Parcel> getLetters(@RequestParam int pageNumber) {
        return parcelRepository.findAllByType(3, PageRequest.of(pageNumber, 10));
    }

}
