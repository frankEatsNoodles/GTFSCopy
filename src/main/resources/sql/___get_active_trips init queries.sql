-- helper table to get_active_trips
CREATE UNLOGGED TABLE trip_bounds (
    trip_id TEXT PRIMARY KEY,
    start_time TEXT,
    end_time TEXT
);

-- insert data
INSERT INTO trip_bounds
SELECT
    trip_id,
    MIN(departure_time),
    MAX(arrival_time)
FROM stop_times
GROUP BY trip_id;


--Index by table for faster read time

-- trips
CREATE INDEX idx_trips_route_service
ON trips (route_id, service_id, trip_id);

-- trip bounds
CREATE INDEX idx_trip_bounds_time
ON trip_bounds (start_time, end_time);

-- calendar
CREATE INDEX idx_calendar_service_dates
ON calendar (service_id, start_date, end_date);

-- calendar_dates
CREATE INDEX idx_calendar_dates_lookup
ON calendar_dates (date, exception_type, service_id);