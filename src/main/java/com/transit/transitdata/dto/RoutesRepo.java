package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutesRepo extends JpaRepository<Routes, String> {
    Optional<Routes> findByRouteId(String routeId);
}
