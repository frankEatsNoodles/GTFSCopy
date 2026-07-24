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
public class RouteService {

    @Autowired
    private DataSource dataSource;

    public void importRoutes() throws Exception {

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
}