-- FUNCTION: public.___get_nearest_stops(double precision, double precision, integer)

-- DROP FUNCTION IF EXISTS public.___get_nearest_stops(double precision, double precision, integer);

CREATE OR REPLACE FUNCTION public.___get_nearest_stops(
	p_lon double precision,
	p_lat double precision,
	p_limit integer)
    RETURNS SETOF stops 
    LANGUAGE 'sql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
    SELECT *
    FROM stops
    ORDER BY location <-> ST_SetSRID(ST_MakePoint(p_lon, p_lat), 4326)
    LIMIT p_limit;
$BODY$;

ALTER FUNCTION public.___get_nearest_stops(double precision, double precision, integer)
    OWNER TO postgres;

