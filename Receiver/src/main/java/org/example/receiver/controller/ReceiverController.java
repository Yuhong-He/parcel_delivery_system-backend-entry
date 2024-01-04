package org.example.receiver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.receiver.entity.Parcel;
import org.example.receiver.entity.ParcelTrack;
import org.example.receiver.message.MQ;
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
public class ReceiverController {

    @Value("${database.address}")
    private String database = "";

    RestTemplate restTemplate = new RestTemplate();

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get parcel tracks", description = "Allowed User gets one parcel tracks")
    @GetMapping("/getHistory")
    public List<ParcelTrack> getHistory(@RequestParam String uuid) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, uuid);
        if (parcel == null)
            return null;
        return parcel.getTracks();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get a parcelList", description = "Allowed student gets their parcels")
    @GetMapping("/getParcelList")
    public String getParcelList(@RequestParam int receiverID, int pageNo) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> responseEntity = template.getForEntity(database + "/parcel/getReceiverParcel?receiverId=" + receiverID +
                "&pageNo=" + pageNo, String.class);
        return responseEntity.getBody();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Confirm address", description = "Allowed student confirm the delivery address")
    @PostMapping("/confirmed")
    public boolean confirmed(@RequestParam int receiverID, String uuid) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, uuid);
        if(parcel != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            parcel.setTracks(List.of(new ParcelTrack("Receiver Confirmed the address", receiverID, formattedDateTime)));
            try {
                MQ.sendToDatabase(parcel);
            } catch (Exception e) {
                log.info("Exception: " + e);
            }
            return true;
        }
        return false;
    }
}
