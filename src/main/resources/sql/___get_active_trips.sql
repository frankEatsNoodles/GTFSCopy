-- FUNCTION: public.___get_active_trips(text, text, text)

-- DROP FUNCTION IF EXISTS public.___get_active_trips(text, text, text);

CREATE OR REPLACE FUNCTION public.___get_active_trips(
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

        -- Regular calendar
        SELECT c.service_id
        FROM calendar c
        WHERE
            (
                (v_day_of_week = 1 AND c.monday = 1) OR
                (v_day_of_week = 2 AND c.tuesday = 1) OR
                (v_day_of_week = 3 AND c.wednesday = 1) OR
                (v_day_of_week = 4 AND c.thursday = 1) OR
                (v_day_of_week = 5 AND c.friday = 1) OR
                (v_day_of_week = 6 AND c.saturday = 1) OR
                (v_day_of_week = 7 AND c.sunday = 1)
            )
        AND c.start_date <= v_date
        AND c.end_date >= v_date

        UNION

        -- Added service
        SELECT cd.service_id
        FROM calendar_dates cd
        WHERE cd.date = v_date
        AND cd.exception_type = 1

        EXCEPT

        -- Removed service
        SELECT cd.service_id
        FROM calendar_dates cd
        WHERE cd.date = v_date
        AND cd.exception_type = 2
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

ALTER FUNCTION public.___get_active_trips(text, text, text)
    OWNER TO postgres;

