package com.transit.transitdata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transit.transitdata.model.StopTimeUpdateRT;
import com.transit.transitdata.model.TripRT;
import com.transit.transitdata.model.VehicleRT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.transit.realtime.GtfsRealtime.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class RTDataService {

    @Value("${vehicle.position.url}")
    private String ocVehiclePositions;

    @Value("{trip.update.url}")
    private String ocTripUpdates;

    @Value("${oc.api.key}")
    private String apiKey;

    public boolean getVehiclePositions() {

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
            return true;
        } catch(Exception e) {
            return false;
        }
    }


    public boolean getTripUpdates() {

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
            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writeValueAsString(tripRTList.get(0));
            System.out.println(json);
            return true;
        } catch(Exception e) {
            return false;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        RTDataService rt = new RTDataService();

        //rt.getVehiclePositions();
        rt.getTripUpdates();

    }


}
