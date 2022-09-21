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
    ('92 Lonsdale Street', NULL, 2, 'Melbourne', 3000),
    ('215 Little Collins Street', NULL, 4, 'Melbourne', 3000),
    ('300 Spencer Street', NULL, 5, 'Melbourne', 3000),
    ('176 Cumberland Street', 'The Rocks', 6, 'Sydney', 2000)
;

INSERT INTO
    app_user(name,email,"password",role)
VALUES
    ('Arman','arman@gmail.com','12345','1'),
    ('Levi','levi@gmail.com','12345','2'),
    ('Saood','saood@gmail.com','12345','2'),
    ('Navdeep','navdeep@gmail.com','12345','2'),
    ('Max','max@gmail.com','12345','3'),
    ('Luke','luke@gmail.com','12345','3'),
    ('Eduardo','aduardo@gmail.com','12345','3')
;
INSERT INTO
    customer(user_id,address,contact,age,is_active)
VALUES
    (5,1, '+614153726767', 25,TRUE),
    (6,2, '+615555555555', 28,TRUE),
    (7,3, '+616666666666', 35,TRUE)
;

INSERT INTO system_admin(user_id) VALUES (1);

INSERT INTO
    hotelier(user_id,is_active)
VALUES
       (2,TRUE),
       (3,TRUE),
       (4,TRUE)
;

INSERT INTO
    hotel_group (name, address, phone)
VALUES
    ('Marriott', 4, '+617676868696'),
    ('Hilton',5,'+615645342312')
;

INSERT INTO hotel_group_hotelier(hotelier_id,hotel_group_id)
VALUES
       (1,1),
       (2,1)
;

INSERT INTO
    hotel(hotel_group_id, name, email, address, contact, city, pin_code,is_active)
VALUES
    (1, 'Melbourne Marriott Hotel', 'melbourne@marriott.com', 1, '+614737373737', 'Melbourne', 3000,TRUE),
    (1, 'Sydney Marriott Hotel', 'sydney@vmarriot.com.au', 2, '+619898989898', 'Sydney', 4000,TRUE),
    (2, 'Perth Hilton Melbourne', 'perth@hilton.com.au', 3, '+614545454545', 'Perth', 5000,FALSE)
;


INSERT INTO
    room_spec(hotel_id, type, max_occupancy, bed_type, room_price)
VALUES
    (1, 'Deluxe', 4, 'Double', 100),
    (1, 'Standard', 2, 'Queen', 200),
    (1, 'Pent House', 3, 'King', 300)
;

INSERT INTO
    room(hotel_id, room_spec_id, number, floor, is_active)
VALUES
    (1, 1, 101, 1, true),
    (1, 1, 102, 1, true),
    (1, 1, 103, 1, true),
    (1, 2, 202, 2, true),
    (1, 2, 402, 4, true),
    (1, 3, 702, 7, true)
;


INSERT INTO feature ("name",description)
    VALUES ('ironing board', 'Pull out table to iron clothes'),
           ('electric kettle','kettle to heat water'),
           ('hair dryer','device to dry hair'),
           ('bath towels','set of towels');

INSERT INTO room_spec_feature(feature_id,room_spec_id,quantity)
    VALUES (1,1,1),
           (1,1,1),
           (1,1,1),
           (1,2,1),
           (2,2,1),
           (2,2,1),
           (2,2,1),
           (3,2,1),
           (3,3,1),
           (3,3,1),
           (3,3,1),
           (4,3,1);

INSERT INTO booking(customer_id,start_date,end_date,is_active,hotel_id)
    VALUES
           (1,DATE '2022-09-27',DATE '2022-09-29',TRUE,1),
           (2,DATE '2022-10-17',DATE '2022-10-18',TRUE,1),
           (2,DATE '2022-08-12',DATE '2022-08-18',TRUE,1),
           (3,DATE '2022-06-05',DATE '2022-06-10',TRUE,1)

;


INSERT INTO room_booking(booking_id,room_id,is_active,main_guest,no_of_guests)
    VALUES
           (1,1,TRUE,'Bertrand Russell',1),
           (1,1,TRUE,'Freddie Mercury',1),
           (1,2,TRUE,'Michael Jackson',2),
           (2,2,TRUE,'Elvis Presley',1),
           (2,2,TRUE,'John Lennon',2),
           (3,3,TRUE,'AR Rahman',2)
;
