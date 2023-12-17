package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.entity.ParcelTrack;
import com.example.parcel_delivery_systembackendentry.service.ParcelTrackService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*provide basic functions here
* more functions to add along with development*/
@RestController
@RequestMapping("/Log")
public class LogController {
    @Resource
    private ParcelTrackService parcelTrackService;

    @PostMapping(value = "/newTrail")
    public int newTrail(@RequestBody ParcelTrack data) {
        return 0;
    }

    @GetMapping(value = "/getReceiverTrail")
    public Result<Object> newTrail(@RequestParam int userId){
        return null;
    }


}
