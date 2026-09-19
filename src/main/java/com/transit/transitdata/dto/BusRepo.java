package com.transit.transitdata.dto;

import com.transit.transitdata.dto.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepo extends JpaRepository<Bus, Long> {

    List<Bus> findByTransitAgency(String transitAgency);

    List<Bus> findByManufacturer(String manufacturer);
}