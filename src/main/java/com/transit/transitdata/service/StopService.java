package com.transit.transitdata.service;

import com.transit.transitdata.dto.Stops;
import com.transit.transitdata.dto.StopsRepo;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.locationtech.jts.geom.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class StopService {

    private final StopsRepo stopsRepo;

    public StopService(StopsRepo stopsRepo) {
        this.stopsRepo = stopsRepo;
    }

    public void importStops() throws IOException {

        GeometryFactory geometryFactory =
                new GeometryFactory(new PrecisionModel(), 4326);

        //Read gtfs data from stops.txt file
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Users\\frank\\Downloads\\GTFSExport\\stops.txt"))) {

            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",", -1);

                Stops stop = new Stops();

                stop.setStopId(fields[0]);
                stop.setStopCode(fields[1]);
                stop.setStopName(fields[2]);
                stop.setTtsStopName(fields[3]);
                stop.setStopDesc(fields[4]);
                stop.setStopLat(Double.parseDouble(fields[5]));
                stop.setStopLon(Double.parseDouble(fields[6]));

                Point point = geometryFactory.createPoint(
                        new Coordinate(Double.parseDouble(fields[6]), Double.parseDouble(fields[5]))
                );

                stop.setLocation(point);
                stop.setZoneId(fields[7]);
                stop.setStopUrl(fields[8]);

                if (!fields[9].isBlank()) {
                    stop.setLocationType(Integer.parseInt(fields[9]));
                }

                if (!fields[10].isBlank()) {
                    stop.setParentStation(fields[10]);
                }

                stop.setStopTimezone(fields[11]);

                if (!fields[12].isBlank()) {
                    stop.setWheelchairBoarding(Integer.parseInt(fields[12]));
                }

                stop.setLevelId(fields[13]);
                stop.setPlatformCode(fields[14]);

                stopsRepo.save(stop);
            }
        }
    }


        

}