package com.transit.transitdata.controller;

import com.transit.transitdata.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/transit")
public class TransitServiceAPIController {

    @Autowired
    DataService dataService;

    @GetMapping("/routes/{routeId}/active-trips")
    public int getActiveTrips(
            @PathVariable String routeId,
            @RequestParam String date,
            @RequestParam String time)   {

        Integer activeTrips;
        LocalDate qDate;
        LocalTime qTime;

        try {

            qDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
            qTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmmss"));
            activeTrips = dataService.dbRoutes(routeId, qDate, qTime);

        } catch (Exception e){
            activeTrips =  -1;
        }

        return activeTrips;
    }

}
