package com.transit.transitdata.service;

import com.transit.transitdata.dto.StopTimes;
import com.transit.transitdata.dto.StopTimesRepo;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class StopTimeService {

    private final StopTimesRepo stopTimesRepo;

    public StopTimeService(StopTimesRepo stopTimesRepo) {
        this.stopTimesRepo = stopTimesRepo;
    }

    public void importStopTimes() throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get("C:\\Users\\frank\\Downloads\\GTFSExport\\stop_times.txt"))) {

            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",", -1);

                StopTimes stopTime = new StopTimes();

                stopTime.setTripId(fields[0]);
                stopTime.setArrivalTime(fields[1]);
                stopTime.setDepartureTime(fields[2]);
                stopTime.setStopId(fields[3]);

                if (!fields[4].isBlank()) {
                    stopTime.setStopSequence(Integer.parseInt(fields[4]));
                }

                stopTime.setStopHeadsign(fields[5]);

                if (!fields[6].isBlank()) {
                    stopTime.setPickupType(Integer.parseInt(fields[6]));
                }

                if (!fields[7].isBlank()) {
                    stopTime.setDropOffType(Integer.parseInt(fields[7]));
                }

                if (!fields[8].isBlank()) {
                    stopTime.setShapeDistTraveled(Double.parseDouble(fields[8]));
                }

                if (fields.length > 9 && !fields[9].isBlank()) {
                    stopTime.setTimepoint(Integer.parseInt(fields[9]));
                }

                stopTimesRepo.save(stopTime);
            }
        }
    }
}