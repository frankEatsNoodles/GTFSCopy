package com.transit.transitdata.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarDatesRepo extends JpaRepository<CalendarDate, CalendarDate.CalendarDatesId> {
}
