package com.transit.transitdata.model;

public class RouteAverageSpeed {

    private String routeId;
    private double averageSpeed;
    private String routeName;
    private String routeColor;

    public RouteAverageSpeed(String routeId, double averageSpeed, String routeName, String routeColor) {
        this.routeId = routeId;
        this.averageSpeed = averageSpeed;
        this.routeName = routeName;
        this.routeColor = routeColor;
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

    public String getRouteColor() {
        return routeColor;
    }

    @Override
    public String toString() {
        return "RouteAverageSpeed{" +
                "routeId='" + routeId + '\'' +
                ", averageSpeed=" + averageSpeed +
                ", routeName='" + routeName + '\'' +
                ", routeColor='" + routeColor + '\'' +
                '}';
    }
}
