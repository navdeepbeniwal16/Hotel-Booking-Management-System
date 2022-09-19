--Search functionality---------------------------------------------
--get all rooms based on hotel id
SELECT r.id as id, t.name as type, h.id as room, max_occupancy, b.name as bed_type, view, room_price
    FROM room_spec r
        JOIN room_type t ON r.type=t.id
        JOIN bed_type b ON r.bed_type=b.id
        JOIN room h ON r.id = h.room_spec_id
    WHERE r.hotel_id = ? AND h.is_active=TRUE;

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
              (DATE '2022-09-30' - '1 day'::interval))
              AND r.is_active = TRUE;

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

--View prior and future hotel bookings-------------------------------
SELECT b.id as id,h.name as hotel_name, start_date, end_date, t.name as room_type, m.id as room_id,no_of_guests,main_guest
    FROM booking b
        JOIN ( room_booking r
            JOIN (room m
                JOIN (room_spec s
                    JOIN room_type t
                    ON t.id = s.type)
                ON m.room_spec_id=s.id)
            ON r.room_id=m.id)
        ON b.id = r.booking_id
        JOIN hotel h ON h.id = b.hotel_id
        JOIN customer c ON c.id=b.customer_id
    WHERE b.customer_id = ? AND b.is_active = TRUE;

--Modify a booking(Add more people)--------------------------------------
UPDATE room_booking SET no_of_guests = 3 WHERE id = ?;

--Modify a booking(change dates)-----------------------------------------
--TODO

--Modify a booking(Cancel Booking)---------------------------------------
UPDATE booking SET is_active = FALSE WHERE id = ?;
UPDATE room_booking SET is_active = FALSE WHERE id = ?;

--Create a hotel for a group--------------------------------------------
--Get Hotel Group
SELECT * FROM HOTEL_GROUP;
--Create Hotel
WITH insert_phone AS (
    INSERT INTO phone (country,area,number)
        VALUES
            (61,414,768594)
        RETURNING id
),
insert_address AS (
    INSERT INTO address (line_1,line_2,district,city,postcode)
        VALUES
            ('1-15','College Cres',(SELECT id from district WHERE name = 'NSW'),'Melbourne',3052)
        RETURNING id
)
INSERT INTO hotel (hotel_group_id, name, email, address, phone,hotel_city,pin_code)
VALUES (1,'JWM Marriott','jwm@gmail.com',(SELECT id FROM insert_address),(SELECT id FROM insert_phone),'Sydney','1052');
--Create hotel room spec
