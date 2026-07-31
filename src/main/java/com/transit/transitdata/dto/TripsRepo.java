package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TripsRepo extends JpaRepository<Trips, String> {
}