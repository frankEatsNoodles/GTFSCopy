package com.transit.transitdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataService {

    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get the schedule routes at time from the db
     *
     * See resources/sql:
     * SELECT COUNT(*) from __get_active_trips('85', '20260717', '14:30:00');
     * returns the number of active scheduled trips
     *
     * @param routeId route id on db table
     * @param date
     * @param time
     * @return
     */
    public Integer dbRoutes(String routeId, LocalDate date, LocalTime time){

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM __get_active_trips(?, ?, ?)",
                Integer.class,
                routeId, dayFormatter.format(date), timeFormatter.format(time)
        );

    }
}
