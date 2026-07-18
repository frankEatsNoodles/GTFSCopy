package com.transit.transitdata.controller;

import com.transit.transitdata.service.RouteService;
import com.transit.transitdata.service.StopService;
import com.transit.transitdata.service.StopTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TransitDataAPIController {


    @Autowired
    StopService stopService;

    @Autowired
    RouteService routeService;

    @Autowired
    StopTimeService stopTimeService;


    @GetMapping("/help")
    public String temp() throws IOException {
        stopService.importStops();
        return "done";
    }

    @GetMapping("/help2")
    public String temp2() throws IOException {
        routeService.importRoutes();
        return "done";
    }

    @GetMapping("/help3")
    public String temp3() throws IOException {
        stopTimeService.importStopTimes();
        return "done";
    }


}
