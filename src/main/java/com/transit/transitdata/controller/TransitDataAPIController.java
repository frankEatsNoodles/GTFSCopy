package com.transit.transitdata.controller;

import com.transit.transitdata.service.DBSetupService;
import com.transit.transitdata.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/setup/init/db/")
public class TransitDataAPIController {


    @Autowired
    DBSetupService dbConnection;

    @Autowired
    DataService dataService;

    @GetMapping("/importStops")
    public String initdbImportStops() throws Exception {
        dbConnection.importAll();
        return "done";
    }

}
