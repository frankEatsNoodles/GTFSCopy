package com.transit.transitdata.service;

import com.transit.transitdata.model.RT.StopTimeUpdateRT;
import com.transit.transitdata.model.RT.TripRT;
import com.transit.transitdata.model.RT.VehicleRT;
import com.transit.transitdata.model.RouteAverageSpeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.google.transit.realtime.GtfsRealtime.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    //update positions every 30 seconds
    @Scheduled(fixedDelay = 30000, initialDelay = 0)
    public void updateBuses() {
        this.updatedVehicles = getVehiclePositions();
        this.updatedTrips = getTripUpdates();
    }

    public List<VehicleRT> getVehiclePositions() {

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

    public List<TripRT> getTripUpdates() {

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

                    LOGGER.info(tripRT.toString());
                    tripRT.setStopTimeUpdateRT(stuRTList);
                    tripRTList.add(tripRT);
                }
            }

            return tripRTList;
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Function that takes the current vehicle positions and returns a list of routeIds sorted by average speed.
     *
     * @param vehicles
     * @return
     */
    public List<RouteAverageSpeed> getAverageSpeedByRoute(List<VehicleRT> vehicles) {

        return vehicles.stream()
                .filter(vehicle ->
                        vehicle.getRouteId() != null &&
                                !vehicle.getRouteId().isBlank()
                )
                .collect(Collectors.groupingBy(
                        VehicleRT::getRouteId,
                        Collectors.averagingDouble(VehicleRT::getSpeed)
                ))
                .entrySet()
                .stream()
                .map(entry -> new RouteAverageSpeed(
                        entry.getKey(),
                        entry.getValue()
                ))
                .sorted(
                        Comparator.comparingDouble(
                                RouteAverageSpeed::getAverageSpeed
                        ).reversed()
                )
                .toList();
    }

//    public static void main(String[] args)  {
//
//        RTDataService rt = new RTDataService();
//
//        //rt.getVehiclePositions();
//        //rt.getTripUpdates();
//
//        System.out.println(rt.getAverageSpeedByRoute(rt.getVehiclePositions()));
//
//    }

}
