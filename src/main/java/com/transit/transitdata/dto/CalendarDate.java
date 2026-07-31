package com.transit.transitdata.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "calendar_dates")
@IdClass(CalendarDate.CalendarDatesId.class)
public class CalendarDate {

    @Id
    @Column(name = "service_id")
    private String serviceId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "exception_type")
    private Integer exceptionType;

    @Data
    public static class CalendarDatesId implements Serializable {
        private String serviceId;
        private LocalDate date;
    }
}