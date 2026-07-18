package com.transit.transitdata.service;

import com.transit.transitdata.dto.StopTimesRepo;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

import javax.sql.DataSource;

@Service
public class StopTimeService {

    private final StopTimesRepo stopTimesRepo;

    public StopTimeService(StopTimesRepo stopTimesRepo) {
        this.stopTimesRepo = stopTimesRepo;
    }

    @Autowired
    private DataSource dataSource;

    public void importStopTimes() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\stop_times.txt"))) {

                copyManager.copyIn("""
                COPY temp_4 (
                    trip_id,
                    arrival_time,
                    departure_time,
                    stop_id,
                    stop_sequence,
                    stop_headsign,
                    pickup_type,
                    drop_off_type,
                    shape_dist_traveled,
                    timepoint
                )
                FROM STDIN
                WITH (
                    FORMAT csv,
                    HEADER true
                )
                """, reader);
            }
        }
    }
}