package com.transit.transitdata.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "trips")
public class Trips {

    @Id
    private String tripId;

    private String routeId;
    private String serviceId;

    private String tripHeadsign;
    private String tripShortName;
    private Integer directionId;
    private String blockId;
    private String shapeId;

    private Integer wheelchairAccessible;
    private Integer bikesAllowed;
}
