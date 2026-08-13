package com.transit.transitdata.service;

import com.transit.transitdata.model.RT.StopTimeUpdateRT;
import com.transit.transitdata.model.RT.TripRT;
import com.transit.transitdata.model.RT.VehicleRT;
import com.transit.transitdata.model.RouteAverageSpeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.google.transit.realtime.GtfsRealtime.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RTDataService {

    private final Logger LOGGER = LoggerFactory.getLogger(RTDataService.class);

    @Value("${vehicle.position.url}")
    private String ocVehiclePositions;

    @Value("${trip.update.url}")
    private String ocTripUpdates;

    @Value("${oc.api.key}")
    private String apiKey;

    private List<VehicleRT> updatedVehicles;

    private List<TripRT> updatedTrips;

    @Autowired
    private DataService db;

    //update positions every 30 seconds
    @Scheduled(fixedDelay = 1500, initialDelay = 0)
    public void updateBuses() {
        this.updatedVehicles = getVehiclePositions();
        this.updatedTrips = getTripUpdates();
    }

    public List<RouteAverageSpeed> getAverageSpeedByRoute() {

        List<VehicleRT> vehicles = this.updatedVehicles;
        List<TripRT> trips = this.updatedTrips;

        Map<String, List<Double>> routeSpeeds = new HashMap<>();
        List<RouteAverageSpeed> averageSpeeds;

        for (VehicleRT vehicle : vehicles) {

            //remove vehicles with no id
            if (vehicle.getVehicleId() == null ||
                    vehicle.getRouteId() == null) {
                continue;
            }

            //map the trip
            TripRT matchingTrip = trips.stream()
                    .filter(trip ->
                            trip.getVehicleId() != null &&
                                    trip.getVehicleId().equals(vehicle.getVehicleId())
                    )
                    .findFirst()
                    .orElse(null);

            if (matchingTrip == null ||
                    matchingTrip.getStopTimeUpdateRT() == null ||
                    matchingTrip.getStopTimeUpdateRT().isEmpty()) {
                continue;
            }

            //Make sure the trip has started (remove trips that hasnt started yet)
            boolean hasStarted = matchingTrip.getStopTimeUpdateRT()
                    .stream()
                    .anyMatch(stop ->
                            stop.getStopsequence() > 1
                    );

            if (hasStarted) {

                routeSpeeds
                        .computeIfAbsent(
                                vehicle.getRouteId(),
                                k -> new ArrayList<>()
                        )
                        .add(vehicle.getSpeed());
            }
        }

        //Calculate speed
        averageSpeeds = routeSpeeds.entrySet()
                .stream()
                .map(entry -> {

                    double averageSpeed = entry.getValue()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    return new RouteAverageSpeed(
                            entry.getKey(),
                            averageSpeed*3.6, //convert m/s to km/h
                            db.getRouteName(entry.getKey())
                    );
                })
                .filter(route -> route.getAverageSpeed() > 0)
                .sorted(
                        Comparator.comparingDouble(
                                RouteAverageSpeed::getAverageSpeed
                        ).reversed()
                )
                .toList();

        return averageSpeeds;
    }

    public List<TripRT> getUpdatedTrips() {
        return updatedTrips;
    }

    public List<VehicleRT> getUpdatedVehicles() {
        return updatedVehicles;
    }

    private List<VehicleRT> getVehiclePositions() {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ocVehiclePositions))
                    .header("Ocp-Apim-Subscription-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "OC Transpo API returned HTTP " + response.statusCode()
                );
            }

            FeedMessage feed = FeedMessage.parseFrom(response.body());

            List<VehicleRT> vehicleRTList = new ArrayList<>();
            VehicleRT vehicleRT;
            VehiclePosition vp;

            for (FeedEntity entity : feed.getEntityList()) {
                if (entity.hasVehicle()) {

                    vp = entity.getVehicle();
                    vehicleRT = new VehicleRT();
                    vehicleRT.setVehicleId(vp.getVehicle().getId());

                    //set trip data
                    if (vp.hasTrip()) {
                        vehicleRT.setTripId(vp.getTrip().getTripId());
                        vehicleRT.setRouteId(vp.getTrip().getRouteId());
                        vehicleRT.setStartTime(vp.getTrip().getStartTime());
                        vehicleRT.setStartDate(vp.getTrip().getStartDate());
                        vehicleRT.setScheduledRelationship(vp.getTrip().getScheduleRelationship().getNumber());
                    }

                    //
                    if (vp.hasPosition()) {
                        vehicleRT.setLongitude(vp.getPosition().getLongitude());
                        vehicleRT.setLatitude(vp.getPosition().getLatitude());
                        vehicleRT.setBearing(vp.getPosition().getBearing());
                        vehicleRT.setSpeed(vp.getPosition().getSpeed());
                    }

                    // timestamp
                    vehicleRT.setTimestamp(vp.getTimestamp());

                    vehicleRTList.add(vehicleRT);
                }
            }
            return vehicleRTList;
        } catch(Exception e) {
            return null;
        }
    }

    private List<TripRT> getTripUpdates() {

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ocTripUpdates))
                    .header("Ocp-Apim-Subscription-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "OC Transpo API returned HTTP " + response.statusCode()
                );
            }

            FeedMessage feed = FeedMessage.parseFrom(response.body());

            List<TripRT> tripRTList = new ArrayList<>();
            TripRT tripRT;
            TripUpdate tu;
            List<StopTimeUpdateRT> stuRTList = new ArrayList<>();
            StopTimeUpdateRT stop;

            for (FeedEntity entity : feed.getEntityList()) {
                if (entity.hasTripUpdate()) {

                    tu = entity.getTripUpdate();
                    tripRT = new TripRT();

                    if (tu.hasTrip()){

                        tripRT.setTripId(tu.getTrip().getTripId());
                        tripRT.setRouteId(tu.getTrip().getRouteId());
                        tripRT.setStartDate(tu.getTrip().getStartDate());
                        tripRT.setStartTime(tu.getTrip().getStartTime());
                        tripRT.setScheduledRelationship(tu.getTrip().getScheduleRelationship().getNumber());
                    }

                    if (tu.hasVehicle()){
                        tripRT.setVehicleId(tu.getVehicle().getId());
                    }

                    for (TripUpdate.StopTimeUpdate stu : tu.getStopTimeUpdateList()){
                        stop = new StopTimeUpdateRT();
                        stop.setStopsequence(stu.getStopSequence());
                        stop.setStopId(stu.getStopId());
                        stop.setArriveTime(stu.getDeparture().getScheduledTime());
                        stop.setScheduleRelationship(stu.getScheduleRelationship().getNumber());
                        stuRTList.add(stop);
                    }

                    tripRT.setStopTimeUpdateRT(stuRTList);
                    tripRTList.add(tripRT);
                }
            }

            return tripRTList;
        } catch(Exception e) {
            return null;
        }
    }

}
