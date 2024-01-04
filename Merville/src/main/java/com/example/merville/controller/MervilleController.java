package com.example.merville.controller;

import com.example.merville.dto.Parcel;
import com.example.merville.dto.ParcelTrack;
import com.example.merville.message.MQ;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class MervilleController {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${database.address}")
    private String database = "";

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Postman System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Merville'>GitHub page</a>.</h2>";
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get parcel tracks", description = "Allowed postman gets one parcel tracks")
    @GetMapping("/getParcelTracks")
    public List<ParcelTrack> getParcelTracks(@RequestParam String parcelId) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, parcelId);
        if (parcel == null)
            return null;
        for (ParcelTrack track: parcel.getTracks()) {
            if (track.getMerville_room()) {
                return parcel.getTracks();
            }
        }
        return null;
    }

    @Operation(description = "Get all letters for a postman")
    @GetMapping(value = "/getParcels")
    public String getParcels(@Parameter(description = "Page number") @RequestParam int pageNo) {
        String endPoint = "/parcel/getMervilleRoomParcels?pageNo=" + pageNo;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(database + endPoint, String.class);
        return responseEntity.getBody();
    }

    @Operation(description = "Record student collected the parcel")
    @PostMapping("/collected")
    public int deliver(@Parameter int mervilleId,
                       @Parameter(description = "updated ParcelTrack with Parcel ID") @RequestParam String parcelId) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, parcelId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        assert parcel != null;
        parcel.setTracks(List.of(new ParcelTrack("Student collected parcel", mervilleId, true, mervilleId, formattedDateTime)));
        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            log.info("Exception during sending Parcel to MQ:" + e);
        }
        return 0;
    }

}
