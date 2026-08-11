package com.transit.transitdata.model;

public class RouteAverageSpeed {

    private String routeId;
    private double averageSpeed;

    public RouteAverageSpeed(String routeId, double averageSpeed) {
        this.routeId = routeId;
        this.averageSpeed = averageSpeed;
    }

    public String getRouteId() {
        return routeId;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    @Override
    public String toString() {
        return "RouteAverageSpeed{" +
                "routeId='" + routeId + '\'' +
                ", averageSpeed=" + averageSpeed +
                '}';
    }
}
