package org.example.receiver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.receiver.dto.CustomPage;
import org.example.receiver.dto.ParcelWithLatestTrack;
import org.example.receiver.entity.Parcel;
import org.example.receiver.entity.ParcelTrack;
import org.example.receiver.message.MQ;
import org.example.receiver.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@RestController
@RequestMapping("/")
public class ReceiverController {

    private final ParcelRepository parcelRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public ReceiverController(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }
    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get parcel tracks", description = "Allowed User gets one parcel tracks")
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@RequestParam String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);;
        if (parcel == null)
            return null;
        return parcel.getTracks();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get a parcelList", description = "Allowed student gets their parcels")
    @GetMapping("/getParcelList")
    public CustomPage getParcelList(@RequestParam int receiverID, int pageNo) {
        int size = 10;
        int skip = (pageNo - 1) * size;
        SkipOperation skipOperation = skip(skip);
        LimitOperation limitOperation = limit(size);

        Criteria criteria = Criteria.where("student").is(receiverID);
        MatchOperation matchOperation = match(criteria);
        AggregationOperation unwind = Aggregation.unwind("tracks");
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "tracks.create_at");
        AggregationOperation group = Aggregation.group("_id")
                .first("type").as("type")
                .first("student").as("student")
                .last("tracks.description").as("lastUpdateDesc")
                .last("tracks.create_at").as("lastUpdateAt");
        AggregationOperation secondSort = Aggregation.sort(Sort.Direction.DESC, "lastUpdateAt");

        Aggregation aggregation = newAggregation(matchOperation, unwind, sort, group, secondSort, skipOperation, limitOperation);

        AggregationResults<ParcelWithLatestTrack> results = mongoTemplate.aggregate(aggregation, "parcel", ParcelWithLatestTrack.class);
        List<ParcelWithLatestTrack> parcels = results.getMappedResults();

        final Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Parcel.class);
        long pages = (long) Math.ceil((double) total / size);

        return new CustomPage(parcels, (int) total, size, pageNo, pages);
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Confirm address", description = "Allowed student confirm the delivery address")
    @PostMapping("/confirmed")
    public boolean confirmed(@RequestParam int receiverID, String uuid) {
        Parcel parcel = parcelRepository.findById(uuid).orElse(null);
        if(parcel != null){
            List<ParcelTrack> parcelTracks = parcel.getTracks();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            ParcelTrack parcelTrack = new ParcelTrack("Receiver Confirmed the address", receiverID, formattedDateTime);
            parcelTracks.add(parcelTrack);
            List<ParcelTrack> newTracks = new ArrayList<>();
            newTracks.add(parcelTrack);
            parcel.setTracks(newTracks);
            Parcel parcel1 = new Parcel(uuid, 1, "", "", receiverID, List.of(new ParcelTrack("Receiver Confirmed the address", receiverID, formattedDateTime)));
            try {
                MQ.sendToDatabase(parcel1);
            } catch (Exception e) {
                log.info("Exception: " + e);
            }
            return true;
        }

        return false;
    }
}
