package com.transit.transitdata.dto;
import org.locationtech.jts.geom.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "temp_1")
public class Stops {

    @Id
    private String stopId;
    private String stopCode;
    private String stopName;
    private String ttsStopName;
    private String stopDesc;
    private double stopLat;
    private double stopLon;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;
    private String zoneId;
    private String stopUrl;
    private Integer locationType;
    private String parentStation;
    private String stopTimezone;
    private Integer wheelchairBoarding;
    private String levelId;
    private String platformCode;
}