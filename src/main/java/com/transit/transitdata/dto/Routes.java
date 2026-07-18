package com.transit.transitdata.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "temp_2")
public class Routes {

    @Id
    private String routeId;
    private String agencyId;
    private String routeShortName;
    private String routeLongName;
    private String routeDesc;
    private Integer routeType;
    private String routeUrl;
    private String routeColor;
    private String routeTextColor;
    private Integer routeSortOrder;
    private Integer continuousPickup;
    private Integer continuousDropOff;
    private String networkId;
}