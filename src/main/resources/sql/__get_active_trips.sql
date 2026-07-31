-- FUNCTION: public.__get_active_trips(text, text, text)

-- DROP FUNCTION IF EXISTS public.__get_active_trips(text, text, text);

CREATE OR REPLACE FUNCTION public.__get_active_trips(
	p_route_id text,
	p_date text,
	p_time text)
    RETURNS TABLE(trip_id character varying, service_id character varying) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
DECLARE
    v_date DATE;
    v_day_of_week INT;
BEGIN
    v_date := TO_DATE(p_date, 'YYYYMMDD');
    v_day_of_week := EXTRACT(ISODOW FROM v_date);

    RETURN QUERY
    WITH active_services AS (
        SELECT c.service_id
        FROM calendar c
        WHERE
            CASE
                WHEN v_day_of_week = 1 THEN c.monday
                WHEN v_day_of_week = 2 THEN c.tuesday
                WHEN v_day_of_week = 3 THEN c.wednesday
                WHEN v_day_of_week = 4 THEN c.thursday
                WHEN v_day_of_week = 5 THEN c.friday
                WHEN v_day_of_week = 6 THEN c.saturday
                WHEN v_day_of_week = 7 THEN c.sunday
            END = 1
        AND c.start_date <= v_date
        AND c.end_date >= v_date

        UNION

        SELECT cd.service_id
        FROM calendar_dates cd
        WHERE cd.date = v_date
        AND cd.exception_type = 1

        EXCEPT

        SELECT cd.service_id
        FROM calendar_dates cd
        WHERE cd.date = v_date
        AND cd.exception_type = 2
    ),

    trip_bounds AS (
        SELECT
            st.trip_id,
            MIN(st.departure_time) AS start_time,
            MAX(st.arrival_time)   AS end_time
        FROM stop_times st
        GROUP BY st.trip_id
    )

    SELECT
        t.trip_id,
        t.service_id
    FROM trips t
    JOIN active_services s ON t.service_id = s.service_id
    JOIN trip_bounds tb ON t.trip_id = tb.trip_id

    WHERE t.route_id = p_route_id
    AND tb.start_time <= p_time
    AND tb.end_time >= p_time;
END;
$BODY$;

ALTER FUNCTION public.__get_active_trips(text, text, text)
    OWNER TO postgres;

