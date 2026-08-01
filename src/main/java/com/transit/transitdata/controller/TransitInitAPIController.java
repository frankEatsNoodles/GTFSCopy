package com.transit.transitdata.controller;

import com.transit.transitdata.service.DBSetupService;
import com.transit.transitdata.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup/init/db/")
public class TransitInitAPIController {


    @Autowired
    DBSetupService dbConnection;

    @Autowired
    DataService dataService;

    /**
     * Setup function for db.
     * Populates the gtfs data tables
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/importStops")
    public String initdbImportStops() throws Exception {
        dbConnection.importAll();
        return "done";
    }

}
