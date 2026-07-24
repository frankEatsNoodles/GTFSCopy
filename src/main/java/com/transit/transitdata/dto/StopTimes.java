package com.transit.transitdata.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stoptimes")
public class StopTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Surrogate key since GTFS has no single primary key

    private String tripId;
    private String arrivalTime;
    private String departureTime;
    private String stopId;
    private Integer stopSequence;
    private String stopHeadsign;
    private Integer pickupType;
    private Integer dropOffType;
    private Double shapeDistTraveled;
    private Integer timepoint;
}