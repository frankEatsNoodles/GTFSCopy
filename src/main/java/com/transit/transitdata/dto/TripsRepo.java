package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripsRepo extends JpaRepository<Trips, String> {

    @Query(value = "SELECT shape_id FROM trips WHERE trip_id = :tripId", nativeQuery = true)
    String findShapeIdfromTripId(@Param("tripId") String tripId);

}