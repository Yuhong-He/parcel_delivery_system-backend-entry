package com.example.postman.controller;

import com.example.postman.dto.Parcel;
import com.example.postman.dto.ParcelTrack;
import com.example.postman.message.MQ;
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
public class PostmanController {

    // The RestTemplate doesn't have much configuration here, so simply define it
    // with new()
    RestTemplate restTemplate = new RestTemplate();

    @Value("${database.address}")
    private String database = "";

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Postman System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Postman'>GitHub page</a>.</h2>";
    }

    @Operation(description = "Get all letters for a postman")
    @GetMapping(value = "/getLetters")
    public String getLetters(@Parameter(description = "Postman's ID") @RequestParam int postmanId,
                             @Parameter(description = "Page number") @RequestParam int pageNo) {
        String endPoint = "/parcel/getLetters?postmanId=" + postmanId + "&pageNo=" + pageNo;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(database + endPoint, String.class);
        return responseEntity.getBody();
    }

    @Operation(description = "Deliver a parcel")
    @PostMapping("/deliver/{postmanId}")
    public int deliver(@PathVariable int postmanId,
            @Parameter(description = "updated ParcelTrack with Parcel ID") @RequestParam String parcelId) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, parcelId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        parcel.setTracks(List.of(new ParcelTrack("Postman delivered the parcel", postmanId, false, postmanId, formattedDateTime)));
        try {
            MQ.sendToDatabase(parcel);
        } catch (Exception e) {
            log.info("Exception during sending Parcel to MQ:" + e);
        }
        return 0;
    }

}
