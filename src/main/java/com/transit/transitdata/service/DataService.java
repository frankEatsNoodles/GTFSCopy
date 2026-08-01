package com.transit.transitdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
     * returns the number of scheduled trips
     *
     * @param routeId route id on db table
     * @param date
     * @param time
     * @return
     */
    public Integer dbRoutes(String routeId, LocalDate date, LocalTime time){

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ___get_active_trips(?, ?, ?)",
                Integer.class,
                routeId, dayFormatter.format(date), timeFormatter.format(time)
        );
    }

    public List<Map<String, Object>> nearestStop(double lon, double lat, int amount){

        return jdbcTemplate.queryForList(
                "SELECT * from  ___get_nearest_stops(?, ?, ?)",
                lon, lat, amount
        );
    }
}
