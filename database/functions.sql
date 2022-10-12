-- Function 1 to check for valid booking
CREATE OR REPLACE FUNCTION check_booking(newValue ANYELEMENT, field ANYELEMENT, booking_id integer)
returns ANYELEMENT
LANGUAGE plpgsql
AS
$$
declare booking_valid BOOLEAN;
BEGIN
SELECT is_active INTO booking_valid FROM booking WHERE id=booking_id;
IF
(booking_valid)
THEN
	RETURN newValue;
ELSE
	ABORT;
END IF;
end;
$$;
--Example of testing function, change hotelid to an is_active state of false and retry. Should abort the request.
-- UPDATE
-- booking
-- SET
-- hotel_id = check_booking(14,hotel_id,1)
-- WHERE id=1;

-- Function 2 to check for valid hotel
CREATE OR REPLACE FUNCTION check_hotel(newValue ANYELEMENT, field ANYELEMENT, hotel_id integer)
returns ANYELEMENT
LANGUAGE plpgsql
AS
$$
declare hotel_valid BOOLEAN;
BEGIN
SELECT is_active INTO hotel_valid FROM hotel WHERE id=hotel_id;
IF
(hotel_valid)
THEN
	RETURN newValue;
ELSE
	ABORT;
END IF;
end;
$$;

-- Function 3 to check for dates clash
CREATE OR REPLACE FUNCTION booking_dates_clash(from_date DATE, to_date DATE, id_hotel INTEGER, id_room INTEGER)
returns BOOLEAN
LANGUAGE plpgsql
AS
$$
declare
specific_booking INTEGER;
	specific_room INTEGER;
BEGIN
FOR specific_booking IN
SELECT id FROM booking WHERE
    start_date
        between
        from_date
        AND
        (to_date - '1 day'::interval)
                          OR
        end_date
            between
                from_date + '1 day'::interval
        AND
        to_date
        AND
        hotel_id=id_hotel
        AND
        is_active = TRUE LOOP
		FOR specific_room IN
SELECT id FROM room_booking WHERE booking_id = specific_booking LOOP
			IF specific_room=id_room
			THEN
				RAISE EXCEPTION 'Room already has a booking';
END IF;
END LOOP;
END LOOP;
RETURN TRUE;
end;
$$;
