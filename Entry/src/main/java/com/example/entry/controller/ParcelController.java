package com.example.entry.controller;

import com.example.entry.common.BaseContext;
import com.example.entry.common.Result;
import com.example.entry.dto.CreateParcelData;
import com.example.entry.dto.CustomPage;
import com.example.entry.dto.ParcelTrack;
import com.example.entry.entity.User;
import com.example.entry.enumeration.ResultCodeEnum;
import com.example.entry.enumeration.UserTypeEnum;
import com.example.entry.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "Parcel", description = "Parcel data controller")
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private UserService userService;

    @Value("${address.estate}")
    private String estateUrl = "";

    @Value("${address.receiver}")
    private String receiverUrl = "";

    @Value("${address.postman}")
    private String postmanUrl = "";

    RestTemplate template = new RestTemplate();

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
    @Operation(summary = "Create a parcel", description = "Allowed Estate Service Staff create a parcel.")
    @PostMapping("/create")
    public Result<Object> create(@RequestBody CreateParcelData data) {
        template.postForEntity(estateUrl + "/createParcel?staff=" + BaseContext.getCurrentId().intValue(), data, String.class);
        return Result.ok();
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Object.class))})
    @Operation(summary = "List parcels", description = "Allowed all type of users list the appropriate parcels based on the user rights.")
    @GetMapping("/list")
    public Result<Object> getParcelList(@Parameter(description = "Page Number") @RequestParam("page") Integer pageNo) {
        User currentUser = userService.getUserById(Math.toIntExact(BaseContext.getCurrentId()));
        if (currentUser.getType() == UserTypeEnum.EstateServiceStaff.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(estateUrl + "/list?page=" + pageNo, String.class);
            return castCustomPage(responseEntity);
        } else if (currentUser.getType() == UserTypeEnum.Postman.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(postmanUrl + "/getLetters?postmanId="  + BaseContext.getCurrentId()
                    + "&pageNo=" + pageNo, String.class);
            return castCustomPage(responseEntity);
        } else if (currentUser.getType() == UserTypeEnum.Student.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(receiverUrl + "/getParcelList?receiverID=" + BaseContext.getCurrentId()
                    + "&pageNo=" + pageNo, String.class);
            return castCustomPage(responseEntity);
        } else {
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }

    private Result<Object> castCustomPage(ResponseEntity<String> responseEntity) {
        String jsonResponse = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Result.ok(objectMapper.readValue(jsonResponse, CustomPage.class));
        } catch (JsonProcessingException e) {
            log.error("Cast CustomPage error: " + e.getMessage());
            return Result.error(e.getMessage(), ResultCodeEnum.FAIL);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
    @Operation(summary = "Postman deliver a parcel", description = "Allowed Postman deliver a parcel.")
    @PostMapping("/deliver")
    public Result<Object> deliver(@RequestParam String parcelId) {
        template.postForEntity(postmanUrl + "/deliver/" + BaseContext.getCurrentId().intValue() + "?parcelId=" + parcelId, null, String.class);
        return Result.ok();
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
    @Operation(summary = "Student confirm address", description = "Allowed student confirm address before delivery.")
    @PostMapping("/confirmAddress")
    public Result<Object> confirmAddress(@RequestParam String parcelId) {
        template.postForEntity(receiverUrl + "/confirmed?receiverId=" + BaseContext.getCurrentId().intValue() + "&parcelId=" + parcelId, null, String.class);
        return Result.ok();
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Object.class))})
    @Operation(summary = "Get tracks for a parcel", description = "Allowed all type of users get the tracks for a parcel.")
    @GetMapping("/tracks")
    public Result<Object> getParcelTracks(@Parameter(description = "Parcel ID") @RequestParam("parcelId") String parcelId) {
        User currentUser = userService.getUserById(Math.toIntExact(BaseContext.getCurrentId()));
        if (currentUser.getType() == UserTypeEnum.EstateServiceStaff.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(estateUrl + "/getParcelTracks?parcelId=" + parcelId, String.class);
            return castParcelTrackList(responseEntity);
        } else if (currentUser.getType() == UserTypeEnum.Postman.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(postmanUrl + "/getParcelTracks?postmanId=" + BaseContext.getCurrentId()
                    + "&parcelId=" + parcelId, String.class);
            return castParcelTrackList(responseEntity);
        } else if (currentUser.getType() == UserTypeEnum.Student.getVal()) {
            ResponseEntity<String> responseEntity = template.getForEntity(receiverUrl + "/getParcelTracks?receiverId=" + BaseContext.getCurrentId()
                    + "&parcelId=" + parcelId, String.class);
            return castParcelTrackList(responseEntity);
        } else {
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }

    private Result<Object> castParcelTrackList(ResponseEntity<String> responseEntity) {
        if (responseEntity.getBody() == null)
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ParcelTrack[] parcelTracksArray = objectMapper.readValue(responseEntity.getBody(), ParcelTrack[].class);
            List<ParcelTrack> parcelTracksList = List.of(parcelTracksArray);
            return Result.ok(parcelTracksList);
        } catch (Exception e) {
            log.error("Cast List<ParcelTrack> error: " + e.getMessage());
            return Result.error(e.getMessage(), ResultCodeEnum.FAIL);
        }
    }
}
