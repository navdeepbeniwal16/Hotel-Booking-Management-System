--Search functionality---------------------------------------------
--get all rooms based on hotel id
SELECT r.id as id, t.name as type, h.id as room, max_occupancy, b.name as bed_type, view, room_price
    FROM room_spec r
        JOIN room_type t ON r.type=t.id
        JOIN bed_type b ON r.bed_type=b.id
        JOIN room h ON r.id = h.room_spec_id
    WHERE r.hotel_id = ?;

--get all rooms booked for the given dates
SELECT r.id AS id
    FROM room_booking r
        JOIN booking b ON b.id = r.booking_id
    WHERE
          (b.start_date between DATE '2022-09-27'
              AND
              (DATE '2022-09-30' - '1 day'::interval)
          OR b.end_date between DATE '2022-09-27'
              AND
              (DATE '2022-09-30' - '1 day'::interval));

--Make a hotel booking----------------------------------------------
WITH insert_booking AS (
    INSERT INTO booking (customer_id, start_date, end_date, is_active, hotel_id)
        VALUES
        (1,DATE '2022-10-15', DATE '2022-10-18',TRUE,1)
    RETURNING id
    )
    INSERT INTO room_booking (booking_id,room_id,is_active,main_guest)
        VALUES
        ((SELECT id FROM insert_booking),1,True,'Ben Stiller');

--Create a hotelier------------------------------
WITH insert_app_user AS (
    INSERT INTO app_user (name,email,password,role)
        VALUES
            ('Harry Potter','harry@gmail.com','ghrs','2')
        RETURNING id
)
INSERT INTO hotelier (user_id, is_active)
VALUES
    ((SELECT id FROM insert_app_user),True);

