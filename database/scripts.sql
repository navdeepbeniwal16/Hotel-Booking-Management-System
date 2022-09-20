--Search functionality---------------------------------------------
--get all rooms based on hotel id
SELECT r.id as id, h.id as room, max_occupancy, bed_type, description, room_price
    FROM room_spec r
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

--Onboard hotelier users------------------------------
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
SELECT b.id as id,h.name as hotel_name, start_date, end_date, s.type as room_type, m.id as room_id,no_of_guests,main_guest
    FROM booking b
        JOIN ( room_booking r
            JOIN (room m
                JOIN room_spec s
                ON m.room_spec_id=s.id)
            ON r.room_id=m.id)
        ON b.id = r.booking_id
        JOIN hotel h ON h.id = b.hotel_id
        JOIN customer c ON c.id=b.customer_id
    WHERE b.customer_id = ? AND b.is_active = TRUE;

--Modify a booking(Add more people)--------------------------------------
UPDATE room_booking SET no_of_guests = 3 WHERE id = ?;

--Modify a booking(change dates)-----------------------------------------
--Check for availability
--Use functions declared above for search functionality
--Change the dates
UPDATE booking SET start_date = DATE '2022-10-30' AND end_date = DATE '2022-11-02';

--Modify a booking(Cancel Booking)---------------------------------------
UPDATE booking SET is_active = FALSE WHERE id = ?;
UPDATE room_booking SET is_active = FALSE WHERE booking_id = ?;

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
INSERT INTO hotel (hotel_group_id, name, email, address, phone,hotel_city,pin_code,is_active)
VALUES (1,'JWM Marriott','jwm@gmail.com',(SELECT id FROM insert_address),(SELECT id FROM insert_phone),'Sydney','1052',true);

--Create hotel rooms-----------------------------------------------------
--Create hotel room spec
INSERT INTO room_spec (hotel_id,type,max_occupancy,bed_type,description,room_price)
VALUES
       (?,
        'Penthouse',
        3,
        'King',
        'Pool Facing',
        450);
--Create hotel room
INSERT INTO room (hotel_id,room_spec_id,number,floor,is_active)
VALUES
    (3,
     (SELECT id FROM room_spec WHERE hotel_id=3 and type = 'Penthouse'),
     901,
     9,
     TRUE);

--View all bookings tied to a hotelier group-------------
SELECT
       b.id AS id,b.customer_id,a.name as customer_name,b.start_date,b.end_date,h.name as hotel_name, g.name as hotel_group_name
    FROM BOOKING b
        JOIN ( hotel h
            JOIN hotel_group g
                ON h.hotel_group_id = g.id)
            ON b.hotel_id=h.id
        JOIN ( customer c
            JOIN app_user a ON c.user_id=a.id)
            ON c.id=b.customer_id
    WHERE b.is_active=TRUE;

--Add remove hoteliers in a hotel group----------------------------------
--Add hoteliers in a hotel group
INSERT INTO hotel_group_hotelier (hotelier_id,hotel_group_id) VALUES (2,1);
--Remove hoteliers from hotel group
DELETE FROM hotel_group_hotelier WHERE hotelier_id = ?;

--Create a hotel group---------------------------------------------------
WITH insert_phone AS (
    INSERT INTO phone (country,area,number)
        VALUES
            (61,437,847586)
        RETURNING id
),
     insert_address AS (
         INSERT INTO address (line_1,line_2,district,city,postcode)
             VALUES
                 ('1-12','Monash Road',(SELECT id from district WHERE name = 'QLD'),'Rawthdowne',1035)
             RETURNING id
     )
INSERT INTO hotel_group (name, address,phone,about)
VALUES ('Novotel Group of Hotels',(SELECT id FROM insert_address),(SELECT id FROM insert_phone),'Chain of Novotel hotels');

--Remove hotel listing--------------------------------------------
--Make hotel inactive
UPDATE hotel SET is_active=FALSE WHERE id=?;
--Make rooms associated with hotel inactive
UPDATE room SET is_active=FALSE WHERE hotel_id=?;
--Make bookings associated with hotel inactive
UPDATE booking SET is_active=FALSE WHERE hotel_id=?;
--Make room bookings associated with inactive booking inactive
UPDATE room_booking SET is_active = FALSE WHERE booking_id IN (SELECT id FROM booking WHERE hotel_id = ?);


