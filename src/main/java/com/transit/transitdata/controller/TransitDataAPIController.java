package com.transit.transitdata.controller;

import com.transit.transitdata.service.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup/init/db/")
public class TransitDataAPIController {


    @Autowired
    DBConnectionService dbConnection;


    @GetMapping("/importStops")
    public String initdbImportStops() throws Exception {
        dbConnection.importAll();
        return "done";
    }

}
