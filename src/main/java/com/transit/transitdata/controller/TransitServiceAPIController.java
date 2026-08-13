package com.transit.transitdata.controller;

import com.transit.transitdata.model.RT.TripRT;
import com.transit.transitdata.model.RT.VehicleRT;
import com.transit.transitdata.model.RouteAverageSpeed;
import com.transit.transitdata.service.DataService;
import com.transit.transitdata.service.RTDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transit")
public class TransitServiceAPIController {

    @Autowired
    DataService dataService;

    @Autowired
    RTDataService rtDataService;

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


    @GetMapping("/routes/{lon}/{lat}/nearest-stop")
    public ResponseEntity<Map<String, Object>> getNearestStop(
            @PathVariable String lon,
            @PathVariable String lat,
            @RequestParam String amount){ //amount of stops to return (default 3)

        Map<String, Object> res = new LinkedHashMap<>();
        ResponseEntity<Map<String, Object>> response;
        int qAmount;
        double qLon;
        double qLat;

        res.put("Lon", lon);
        res.put("Lat", lat);

        //Set query amount
        try {
            qAmount = Integer.parseInt(amount);
            if (qAmount <= 0){
                qAmount = 3;
            }
        } catch (Exception e){
            qAmount = 3;
        }

        res.put("amount", qAmount);

        //Query db
        try {
            qLon = Double.parseDouble(lon);
            qLat = Double.parseDouble(lat);

            res.put("stops",dataService.nearestStop(qLon, qLat, qAmount));
            response = ResponseEntity.ok(res);
        } catch(NumberFormatException e){ //400
            res.put("error", "Invalid Longitude and Latitude values");
            response = ResponseEntity.badRequest().body(res);
        } catch (Exception e){ //400
            res.put("error", "Internal server error");
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        return response;
    }

    @GetMapping("/live/vehicleposition")
    public ResponseEntity<List<VehicleRT>> getVehiclePosition(){
        return ResponseEntity.ok(rtDataService.getUpdatedVehicles());
    }

    @GetMapping("/live/tripupdate")
    public ResponseEntity<List<TripRT>> getTripUpdates() {
        return ResponseEntity.ok(rtDataService.getUpdatedTrips());
    }

    @GetMapping("/live/average")
    public ResponseEntity<List<RouteAverageSpeed>> getAverageBusSpeed(){
        return ResponseEntity.ok(rtDataService.getAverageSpeedByRoute());
    }

}
