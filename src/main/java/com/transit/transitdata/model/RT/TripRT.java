package com.transit.transitdata.model.RT;

import java.util.List;

public class TripRT {

    private String tripId;
    private String routeId;
    private String startTime;
    private String startDate;
    private int scheduledRelationship;

    private String vehicleId; //same as vehicle

    private List<StopTimeUpdateRT> stopTimeUpdateRT;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getScheduledRelationship() {
        return scheduledRelationship;
    }

    public void setScheduledRelationship(int scheduledRelationship) {
        this.scheduledRelationship = scheduledRelationship;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<StopTimeUpdateRT> getStopTimeUpdateRT() {
        return stopTimeUpdateRT;
    }

    public void setStopTimeUpdateRT(List<StopTimeUpdateRT> stopTimeUpdateRT) {
        this.stopTimeUpdateRT = stopTimeUpdateRT;
    }

    @Override
    public String toString() {
        return "TripRT{" +
                "tripId='" + tripId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", startDate='" + startDate + '\'' +
                ", scheduledRelationship=" + scheduledRelationship +
                ", vehicleId='" + vehicleId + '\'' +
                ", stopTimeUpdateRT=" + stopTimeUpdateRT +
                '}';
    }
}
