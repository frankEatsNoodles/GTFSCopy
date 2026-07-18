package com.transit.transitdata.service;

import com.transit.transitdata.dto.Routes;
import com.transit.transitdata.dto.RoutesRepo;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class RouteService {

    private final RoutesRepo routesRepo;

    public RouteService(RoutesRepo routesRepo) {
        this.routesRepo = routesRepo;
    }

    public void importRoutes() throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get("C:\\Users\\frank\\Downloads\\GTFSExport\\routes.txt"))) {

            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",", -1);

                Routes route = new Routes();

                route.setRouteId(fields[0]);
                route.setAgencyId(fields[1]);
                route.setRouteShortName(fields[2]);
                route.setRouteLongName(fields[3]);
                route.setRouteDesc(fields[4]);

                if (!fields[5].isBlank()) {
                    route.setRouteType(Integer.parseInt(fields[5]));
                }

                route.setRouteUrl(fields[6]);
                route.setRouteColor(fields[7]);
                route.setRouteTextColor(fields[8]);

                if (!fields[9].isBlank()) {
                    route.setRouteSortOrder(Integer.parseInt(fields[9]));
                }

                if (!fields[10].isBlank()) {
                    route.setContinuousPickup(Integer.parseInt(fields[10]));
                }

                if (!fields[11].isBlank()) {
                    route.setContinuousDropOff(Integer.parseInt(fields[11]));
                }

                if (fields.length > 12 && !fields[12].isBlank()) {
                    route.setNetworkId(fields[12]);
                }

                routesRepo.save(route);
            }
        }
    }
}