package com.example.entry.controller;

import com.example.entry.common.BaseContext;
import com.example.entry.common.Result;
import com.example.entry.dto.CreateParcelData;
import com.example.entry.dto.CustomPage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@Tag(name = "Parcel", description = "Parcel data controller")
@RequestMapping("/parcel")
public class ParcelController {

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
                    schema = @Schema(implementation = Object.class))})
    @Operation(summary = "List parcels", description = "Allowed all type of users list the appropriate parcels based on the user rights.")
    @GetMapping("/list")
    public Result<Object> getParcelList(@Parameter(description = "Page Number") @RequestParam("page") Integer pageNo) {
        User currentUser = userService.getUserById(Math.toIntExact(BaseContext.getCurrentId()));
        if (currentUser.getType() == UserTypeEnum.EstateServiceStaff.getVal()) {
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> responseEntity = template.getForEntity("http://localhost:18082/estate/list?page=" + pageNo, String.class);
            String jsonResponse = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return Result.ok(objectMapper.readValue(jsonResponse, CustomPage.class));
            } catch (JsonProcessingException e) {
                log.error("Cast CustomPage error: " + e.getMessage());
                return Result.error(e.getMessage(), ResultCodeEnum.FAIL);
            }
        } else {
            return Result.error(ResultCodeEnum.NO_PERMISSION);
        }
    }
}
