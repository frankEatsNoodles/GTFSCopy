package com.transit.transitdata.service;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;


@Service
public class DBSetupService {

    private final Logger LOGGER = LoggerFactory.getLogger(DBSetupService.class);

    @Autowired
    private DataSource dataSource;

    public void importAll() throws Exception {
        this.importRoutes();
        LOGGER.info("import routes done");
        this.importStops();
        LOGGER.info("import stops done");
        this.importCalendar();
        LOGGER.info("import calendar done");
        this.importStopTimes();
        LOGGER.info("import stop times done");
        this.importCalendarDates();
        LOGGER.info("import calendar dates done");
        this.importTrips();
        LOGGER.info("import trips done");
    }

    private void importRoutes() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\routes.txt"))) {

                copyManager.copyIn("""
                    COPY routes (
                        route_id,
                        agency_id,
                        route_short_name,
                        route_long_name,
                        route_desc,
                        route_type,
                        route_url,
                        route_color,
                        route_text_color,
                        route_sort_order,
                        continuous_pickup,
                        continuous_drop_off,
                        network_id
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

    private void importStops() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\stops.txt"))) {

                copyManager.copyIn("""
                    COPY stops (
                        stop_id,
                        stop_code,
                        stop_name,
                        tts_stop_name,
                        stop_desc,
                        stop_lat,
                        stop_lon,
                        zone_id,
                        stop_url,
                        location_type,
                        parent_station,
                        stop_timezone,
                        wheelchair_boarding,
                        level_id,
                        platform_code
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

    private void importCalendar() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\calendar.txt"))) {

                copyManager.copyIn("""
                        COPY calendar (
                            service_id,
                            monday,
                            tuesday,
                            wednesday,
                            thursday,
                            friday,
                            saturday,
                            sunday,
                            start_date,
                            end_date
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

    private void importStopTimes() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\stop_times.txt"))) {

                copyManager.copyIn("""
                COPY stop_times (
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

    private void importCalendarDates() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\calendar_dates.txt"))) {

                copyManager.copyIn("""
                        COPY calendar_dates (
                            service_id,
                            date,
                            exception_type
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

    private void importTrips() throws Exception {

        try (Connection conn = dataSource.getConnection()) {

            CopyManager copyManager =
                    conn.unwrap(PGConnection.class).getCopyAPI();

            try (Reader reader = Files.newBufferedReader(
                    Path.of("C:\\Users\\frank\\Downloads\\GTFSExport\\trips.txt"))) {

                copyManager.copyIn("""
            COPY trips (
                route_id,
                service_id,
                trip_id,
                trip_headsign,
                trip_short_name,
                direction_id,
                block_id,
                shape_id,
                wheelchair_accessible,
                bikes_allowed
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