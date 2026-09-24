package com.transit.transitdata.service;

import com.transit.transitdata.dto.Bus;
import com.transit.transitdata.dto.BusRepo;
import com.transit.transitdata.dto.RoutesRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private BusRepo busRepo;

    @Autowired
    private RoutesRepo routesRepo;

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

    /**
     *
     * @param lon
     * @param lat
     * @param amount
     * @return
     */
    public List<Map<String, Object>> nearestStop(double lon, double lat, int amount){

        return jdbcTemplate.queryForList(
                "SELECT * from  ___get_nearest_stops(?, ?, ?)",
                lon, lat, amount
        );
    }

    public String getRouteName(String routeId) {
        try{
            return jdbcTemplate.queryForObject("SELECT route_long_name FROM public.routes where route_id = ?", String.class, routeId);
        }catch(Exception e){
            return "No route name";
        }
    }

    public String getRouteColor(String routeId) {
        try{
            return jdbcTemplate.queryForObject("SELECT route_color FROM public.routes where route_id = ?", String.class, routeId);
        }catch(Exception e){
            return "No route color";
        }
    }

    public List<Map<String, Object>> getShape(String shapeId) {
        return jdbcTemplate.queryForList(
                "SELECT * FROM shapes WHERE shape_id = ? ORDER BY sequence",shapeId);
    }

    //returns the list of buses in the transit agency
    public List<Bus> getBusFleet(String agency){
        return busRepo.findByTransitAgency(agency);
    }
}
