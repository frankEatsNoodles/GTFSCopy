package com.transit.transitdata.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_agency", nullable = false)
    private String transitAgency;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(name = "fleet_number_start", nullable = false)
    private Integer fleetNumberStart;

    @Column(name = "fleet_number_end", nullable = false)
    private Integer fleetNumberEnd;

    public Bus() {
    }

    public Bus(
            String transitAgency,
            String manufacturer,
            String model,
            Integer fleetNumberStart,
            Integer fleetNumberEnd
    ) {
        this.transitAgency = transitAgency;
        this.manufacturer = manufacturer;
        this.model = model;
        this.fleetNumberStart = fleetNumberStart;
        this.fleetNumberEnd = fleetNumberEnd;
    }

    public Long getId() {
        return id;
    }

    public String getTransitAgency() {
        return transitAgency;
    }

    public void setTransitAgency(String transitAgency) {
        this.transitAgency = transitAgency;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getFleetNumberStart() {
        return fleetNumberStart;
    }

    public void setFleetNumberStart(Integer fleetNumberStart) {
        this.fleetNumberStart = fleetNumberStart;
    }

    public Integer getFleetNumberEnd() {
        return fleetNumberEnd;
    }

    public void setFleetNumberEnd(Integer fleetNumberEnd) {
        this.fleetNumberEnd = fleetNumberEnd;
    }
}