package com.transit.transitdata.model;

public class RouteAverageSpeed {

    private String routeId;
    private double averageSpeed;
    private String routeName;

    public RouteAverageSpeed(String routeId, double averageSpeed, String routeName) {
        this.routeId = routeId;
        this.averageSpeed = averageSpeed;
        this.routeName = routeName;
    }

    public String getRouteId() {
        return routeId;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public String getRouteName() {
        return routeName;
    }

    @Override
    public String toString() {
        return "RouteAverageSpeed{" +
                "routeId='" + routeId + '\'' +
                ", averageSpeed=" + averageSpeed +
                '}';
    }
}
