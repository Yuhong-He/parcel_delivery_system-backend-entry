package com.example.postman.controller;

import com.example.postman.dto.Parcel;
import com.example.postman.dto.ParcelTrack;
import com.example.postman.message.MQ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Postman")
public class PostmanController {

    // The RestTemplate doesn't have much configuration here, so simply define it
    // with new()
    RestTemplate restTemplate = new RestTemplate();

    @Value("${database.address}")
    private String database = "";

    @Operation(description = "Get all letters for a postman")
    @GetMapping(value = "/getLetters")
    public List<Parcel> getLetters(@Parameter(description = "postman's id") @RequestParam int id) {
        List<Parcel> result = new ArrayList<>();
        String endPoint = "/parcel/getLetters?pageNumber=1&pageSize=5";
        List<Parcel> parcels = restTemplate.getForObject(database + endPoint, List.class);
        assert parcels != null;
        for (Parcel parcel : parcels) {
            List<ParcelTrack> parcelTracks = parcel.getTracks();
            if (parcelTracks.get(parcelTracks.size() - 1).getPostman() == id)
                result.add(parcel);
        }
        return result;
    }

    @Operation(description = "Deliver a parcel")
    @PostMapping("/deliver/{Id}")
    public int deliver(@RequestParam int postmanId,
            @Parameter(description = "updated ParcelTrack with Parcel ID") @RequestBody Parcel parcel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        ParcelTrack parcelTrack = new ParcelTrack("Postman delivered the parcel", postmanId, formattedDateTime);
        List<ParcelTrack> parcelTracks = new ArrayList<>();
        parcelTracks.add(parcelTrack);
        parcel.setTracks(parcelTracks);
        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            log.info("Exception during sending Parcel to MQ:" + e);
        }
        return 0;
    }

}
