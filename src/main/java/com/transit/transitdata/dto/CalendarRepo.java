package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepo extends JpaRepository<Calendar, String> {
}