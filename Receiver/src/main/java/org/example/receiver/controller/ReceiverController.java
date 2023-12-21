package org.example.receiver.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.beust.jcommander.internal.Lists;
import com.example.estate.dto.CustomPage;
import com.example.estate.dto.ParcelWithLatestTrack;
import com.example.estate.dto.ParcelWithStudentInfo;
import com.example.estate.dto.StudentInfo;
import com.example.estate.entity.User;
import com.example.estate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bson.types.ObjectId;
import org.example.receiver.entity.Parcel;
import org.example.receiver.entity.ParcelTrack;
import org.example.receiver.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.mongodb.client.model.Aggregates.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/TransportationBroker")
public class ReceiverController {
    private final ParcelRepository parcelRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    private final AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
    private final AggregationOperation unwind = Aggregation.unwind("tracks");
    private final AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "latestTrack.create_at");

    private final AggregationOperation group = Aggregation.group("_id")
            .first("type").as("type")
            .first("address1").as("address1")
            .first("address2").as("address2")
            .first("student").as("student")
            .first("tracks").as("latestTrack");


    @Autowired
    public ReceiverController(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get parce traces", description = "Allowed User gets one parcel tracks")
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@RequestParam String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);;
        if(parcel == null){return null;}
        return parcel.getTracks();
    }

    @RequestMapping("/AAA")
    public String AAA() {
        return "hellow";
    }
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get a parcelList", description = "Allowed User gets their parcels")
    @GetMapping("/getParcelList")
    public CustomPage getParcelList(@RequestParam int receiverID, int pageNo, String lastId) {

        final Criteria criteriaDefinition = Criteria.where("student").is(receiverID);
        final Query query = new Query(criteriaDefinition).with(Sort.by(Sort.Order.desc("_id")));
        long total = mongoTemplate.count(query, Parcel.class);
        int pageSize = 10;
        int pages = (int) Math.ceil(total / (double) pageSize);
        if (pageNo<=0 || pageNo> pages) {
            pageNo = 1;
        }

        if (StringUtils.isNotBlank(lastId)) {
            if (pageNo != 1) {
                criteriaDefinition.and("_id").lt(new ObjectId(lastId));
            }
            query.limit(pageSize);
        } else {
            int skip = pageSize * (pageNo - 1);
            query.skip(skip).limit(pageSize);
        }
        List<Parcel> newList = mongoTemplate.find(query, Parcel.class);
        return new CustomPage(newList, total, pageSize, pageNo, pages);




//        AggregationOperation matchid = Aggregation.match(Criteria.where("parcel.student").is(receiverID));
//        Aggregation aggregation = newAggregation(matchid, unwind, sort, group, secondSort);
//        AggregationResults<ParcelWithLatestTrack> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelWithLatestTrack.class);
//        List<ParcelWithLatestTrack> parcels = results.getMappedResults();
//        int total = parcels.size();
//        int pageSize = 10;
//        int fromIndex = Math.min((pageNo - 1) * pageSize, total);
//        int toIndex = Math.min(fromIndex + pageSize, total);
//        List<ParcelWithLatestTrack> list = parcels.subList(fromIndex, toIndex);
//        long pages = (long) Math.ceil((double) total / pageSize);
//        return new CustomPage(list, total, pageSize, pageNo, pages);

    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get notifications", description = "Allowed User gets notifications")
    @GetMapping("/getNotification")
    public List<Parcel> getNotification(@RequestParam int receiverID) {
        AggregationOperation matchid = Aggregation.match(Criteria.where("parcel.student").is(receiverID));
        AggregationOperation matchdes = Aggregation.match(Criteria.where("tracks.description").is("to be confirmed"));
        Aggregation aggregation = newAggregation(matchid, unwind, sort, matchdes,group, secondSort);
        AggregationResults<ParcelWithLatestTrack> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelWithLatestTrack.class);
        List<ParcelWithLatestTrack> parcels = results.getMappedResults();

        return null;
    }
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "get a parcelList", description = "Allowed User gets their parcels")
    @GetMapping("/confirmed")
    public boolean confirmed(@RequestParam int receiverID, String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);;
        if(parcel != null){
            List<ParcelTrack> parcelTracks = parcel.getTracks();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            ParcelTrack parcelTrack = new ParcelTrack("confirmed", receiverID, formattedDateTime);
            parcelTracks.add(parcelTrack);
            parcelRepository.save(parcel);
            return true;
        }
        return false;
    }
}
