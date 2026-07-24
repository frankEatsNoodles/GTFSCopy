package com.transit.transitdata.service;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;

@Service
public class StopService {

    @Autowired
    private DataSource dataSource;

    public void importStops() throws Exception {

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
}