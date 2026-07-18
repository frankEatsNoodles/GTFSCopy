package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StopTimesRepo extends JpaRepository<StopTimes, Long> {
}