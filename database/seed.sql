INSERT INTO
    district(name)
VALUES
    ('VIC'),
    ('NSW'),
    ('NT'),
    ('QLD'),
    ('SA'),
    ('ACT'),
    ('WA'),
    ('TAS')
;

INSERT INTO
    address(line_1, line_2, district, city, postcode)
VALUES
    ('50 Batman St', NULL, 1, 'West Melbourne', 3003),
    ('92 Lonsdale Street', NULL, 1, 'Melbourne', 3000),
    ('215 Little Collins Street', NULL, 1, 'Melbourne', 3000),
    ('300 Spencer Street', NULL, 1, 'Melbourne', 3000),
    ('176 Cumberland Street', 'The Rocks', 1, 'Sydney', 2000)
;

INSERT INTO
    phone(country, area, number)
VALUES
    (61, 3, 96623900),
    (61, 2, 98975983)
;

INSERT INTO
    hotel_group (name, address, phone)
VALUES
    ('Marriott', 1, 1);

INSERT INTO
    business_detail_type(name)
VALUES ('ABN'), ('ACN'), ('ASIC Entity Name');

INSERT INTO
    hotel_group_business_detail(hotel_group, type, detail)
VALUES (1, 1, '38 094 477 175'), (1, 3, 'LONEX PTY LIMITED');

INSERT INTO
    hotel(hotel_group_id, name, email, address, phone, hotel_city, pin_code)
VALUES
    (1, 'Melbourne Marriott Hotel', 'melbourne@marriott.com', 2, 1, 'Melbourne', 3000),
    (1, 'The Victoria Hotel', 'hello@victoriahotel.com.au', 3, 1, 'Melbourne', 3000),
    (1, 'Atlantis Hotel Melbourne', 'help@atlantishotel.com.au', 4, 1, 'Melbourne', 3000),
    (1, 'Shangri-La Sydney', 'hello@shangrila.com.au', 5, 2, 'Sydney', 2000)
;

INSERT INTO
    room_spec(hotel_id, type, max_occupancy, bed_type, description, room_price)
VALUES
    (1, 'Standard', 2, 'Single', 'Cozy single room for one or two people', 100),
    (1, 'Deluxe', 2, 'Double', 'Extra spacious room to treat yourself', 200),
    (1, 'Premium', 3, 'Queen', 'The perfect room for business traveller', 300)
;

INSERT INTO
    room(hotel_id, room_spec_id, floor, number, is_active)
VALUES
    (1, 2, 0, 1, true),
    (1, 3, 0, 2, true)
;

INSERT INTO
    app_user(name,email,"password",role)
VALUES
    ('Arman','arman@gmail.com','12345','2')
;

INSERT INTO
    hotelier(user_id,is_active)
VALUES(1,TRUE)
;

INSERT INTO
    app_user(name,email,"password",role)
VALUES
    ('Saood','saood@gmail.com','test','3')
;

INSERT INTO
    customer(user_id,address,contact,age)
VALUES(2,2, '+614153726767', 1)
;

-- New queries start here --------------------

INSERT INTO
    app_user(name,email,"password",role)
VALUES
    ('Ben','ben@gmail.com','pwd','3')
;


INSERT INTO
    customer(user_id,address,contact,age)
VALUES(3,1, '+61415678394', 25)
;

INSERT INTO amenity("name", description)
    VALUES
           ('Gym', 'Utility space for exercise'),
           ('Spa','Utility space for spa/sauna/massage'),
           ('restaurant','eatery spot for in room dining');

INSERT INTO hotel_amenity(amenity_id,hotel_id)
    VALUES
           (1,1),
           (2,1),
           (3,1);

INSERT INTO app_user("name", email, "password", "role")
    VALUES ('Chris','chris@gmail.com','tbdpwd',1);

INSERT INTO system_admin(user_id) VALUES (4);

INSERT INTO hotel_group_hotelier(hotelier_id,hotel_group_id)
    VALUES (1,1);

INSERT INTO feature ("name",description)
    VALUES ('ironing board', 'Pull out table to iron clothes'),
           ('electric kettle','kettle to heat water'),
           ('hair dryer','device to dry hair'),
           ('bath towels','set of towels');

INSERT INTO room_feature(feature_id,room_spec_id,quantity)
    VALUES (1,1,1),
           (2,1,1),
           (3,1,1),
           (4,1,1),
           (1,2,1),
           (2,2,1),
           (3,2,1),
           (4,2,1),
           (1,3,1),
           (2,3,1),
           (3,3,1),
           (4,3,1);

INSERT INTO booking(customer_id,start_date,end_date,is_active,hotel_id)
    VALUES (2,DATE '2022-09-27',DATE '2022-09-29',TRUE,1);

INSERT INTO booking(customer_id,start_date,end_date,is_active,hotel_id)
    VALUES (1,DATE '2022-09-29',DATE '2022-09-30',TRUE,1);

INSERT INTO room_booking(booking_id,room_id,is_active,main_guest)
    VALUES (1,1,TRUE,'Tim Burners Lee');

INSERT INTO room_booking(booking_id,room_id,is_active,main_guest)
    VALUES (2,1,TRUE,'Bertrand Russell');

UPDATE ROOM_BOOKING SET no_of_guests=2 WHERE id=1;
UPDATE ROOM_BOOKING SET no_of_guests=2 WHERE id=2;
UPDATE ROOM_BOOKING SET no_of_guests=2 WHERE id=3;