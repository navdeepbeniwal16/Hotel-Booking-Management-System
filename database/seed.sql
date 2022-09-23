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
    ('12 Bourke St', '', 1, 'Melbourne', 3053),
    ('23 Collins St', '', 2, 'Sydney', 5035),
    ('34 Elizabeth St', '', 3, 'Darwin', 4024),
    ('45 Exhibition St', '', 4, 'Brisbane', 2402),
    ('56 Flinders St', '', 5, 'Adelaide', 9809),
    ('67 King St', '', 6, 'Canberra', 8908),
    ('78 La Trobe St', '', 7, 'Perth', 5605),
    ('90 Lonsdale St', '', 8, 'Hobart', 6506),
    ('21 Victoria St', '', 1, 'Melbourne', 7307),
    ('32 Queen Street', '', 2, 'Sydney', 3707),
    ('43 Russel Street', '', 3, 'Darwin', 6106),
    ('54 Spencer Street', '', 4, 'Brisbane', 1601),
    ('65 Beckett Street', '', 5, 'Adelaide', 5404),
    ('13 Abbey St', '', 1, 'Melbourne', 3078),
    ('24 Downing St', '', 1, 'Melbourne', 6788),
    ('35 Baker St', '', 1, 'Melbourne', 8398),
    ('46 Oxford St', '', 1, 'Melbourne', 1782),
    ('57 Bond St', '', 2, 'Sydney', 0873),
    ('68 Fleet St', '', 2, 'Sydney', 7880),
    ('79 Regent St', '', 2, 'Sydney', 8909),
    ('80 Strand St', '', 7, 'Perth', 4151),
    ('91 Church St', '', 7, 'Perth', 1324),
    ('02 Albany St', '', 7, 'Perth', 3231),
    ('31 Carnaby St', '', 7, 'Perth', 6446),
    ('42 Wall St', '', 4, 'Brisbane', 9209),
    ('53 Canal St', '', 4, 'Brisbane', 1733),
    ('64 Pineapple St', '', 4, 'Brisbane', 1231)

;

INSERT INTO
    app_user(name,email,"password",role)
VALUES
    ('Admin','admin@gmail.com','admin',1),
    ('Arman','arman@gmail.com','arman',2),
    ('Levi','levi@gmail.com','levi',2),
    ('Saood','saood@gmail.com','saood',2),
    ('Navdeep','navdeep@gmail.com','navdeep',2),
    ('Max','max@gmail.com','max',2),
    ('Luke','luke@gmail.com','luke',2),
    ('Eduardo','eduardo@gmail.com','eduardo',2),
    ('Daniel','daniel@gmail.com','daniel',2),
    ('Matthew','matthew@gmail.com','matthew',2),
    ('Anthony','anthony@gmail.com','anthony',2),
    ('Mark','mark@gmail.com','mark',2),
    ('Donald','donald@gmail.com','donald',2),
    ('Robert','robert@gmail.com','robert',3),
    ('John','john@gmail.com','john',3),
    ('Michael','michael@gmail.com','michael',3),
    ('David','david@gmail.com','david',3),
    ('William','william@gmail.com','william',3),
    ('Richard','richard@gmail.com','richard',3),
    ('Joseph','joseph@gmail.com','joseph',3),
    ('Thomas','thomas@gmail.com','thomas',3)
;
INSERT INTO
    customer(user_id,address,contact,age,is_active)
VALUES
    (13,1, '+614153726767', 25,TRUE),
    (14,2, '+615676546734', 31,TRUE),
    (15,3, '+613456783245', 18,TRUE),
    (16,4, '+617492838487', 56,TRUE),
    (17,5, '+618492840284', 42,TRUE),
    (18,6, '+612387242240', 75,TRUE),
    (19,7, '+612809238924', 98,TRUE),
    (20,8, '+611742681943', 26,TRUE)
;

INSERT INTO system_admin(user_id) VALUES (1);

INSERT INTO
    hotelier(user_id,is_active)
VALUES
       (2,TRUE),
       (3,TRUE),
       (4,TRUE),
       (5,TRUE),
       (6,TRUE),
       (7,TRUE),
       (8,TRUE),
       (9,TRUE),
       (10,TRUE),
       (11,TRUE),
       (12,TRUE)
;

INSERT INTO
    hotel_group (name, address, phone)
VALUES
    ('Marriott', 9, '+617676868696'),
    ('Hilton',10,'+615645342312'),
    ('Hyatt',11,'+615645342312'),
    ('Four Seasons',12,'+615645342312'),
    ('Novotel',13,'+615645342312')
;

INSERT INTO hotel_group_hotelier(hotelier_id,hotel_group_id)
VALUES
       (1,1),
       (2,1),
       (3,1),
       (4,2),
       (5,2),
       (6,3),
       (7,3),
       (8,3),
       (9,3),
       (10,4),
       (11,5)
;

INSERT INTO
    hotel(hotel_group_id, name, email, address, contact, city, pin_code,is_active)
VALUES
    (1, 'Melbourne Marriott', 'melbourne@marriott.com', 14, '+614737373737', 'Melbourne', 3000,TRUE),
    (1, 'Sydney Marriott', 'sydney@marriott.com.au', 18, '+619898989898', 'Sydney', 4000,TRUE),
    (1, 'Brisbane Marriott', 'brisbane@mariott.com.au', 25, '+614545454545', 'Brisbane', 5000,TRUE),
    (1, 'Perth Marriot', 'perth@mariott.com.au', 21, '+618375647893', 'Perth', 6000,TRUE),
    (2, 'Melbourne Hilton', 'melbourne@hilton.com.au', 15, '+61765432123', 'Melbourne', 7000,TRUE),
    (2, 'Sydney Hilton', 'sydney@hilton.com.au', 19, '+612345678901', 'Sydney', 8000,TRUE),
    (2, 'Brisbane Hilton', 'brisbane@hilton.com.au', 26, '+613456789012', 'Brisbane', 9000,TRUE),
    (2, 'Perth Hilton', 'perth@hilton.com.au', 22, '+614567890123', 'Perth', 9999,TRUE),
    (3, 'Melbourne Hyatt', 'melbourne@hyatt.com.au', 16, '+615678901234', 'Melbourne', 8888,TRUE),
    (3, 'Sydney Hyatt', 'sydney@hyatt.com.au', 20, '+616789012345', 'Sydney', 7777,TRUE),
    (4, 'Brisbane Four Seasons', 'brisbane@fourseasons.com.au', 27, '+617890123456', 'Brisbane', 6666,TRUE),
    (4, 'Perth Four Seasons', 'perth@fourseasons.com.au', 23, '+618901234567', 'Perth', 5555,FALSE),
    (5, 'Perth Novotel', 'perth@novotel.com.au', 24, '+619012345678', 'Perth', 4444,TRUE),
    (5, 'Melbourne Novotel', 'melbourne@novotel.com.au', 17, '+610123456789', 'Melbourne', 3333,FALSE)
;


INSERT INTO
    room_spec(hotel_id, type, max_occupancy, bed_type, room_price)
VALUES
    (1, 'Standard', 2, 'Double', 100),
    (1, 'Deluxe', 3, 'Queen', 200),
    (1, 'Suite', 3, 'King', 300),
    (1, 'Studio', 1, 'Single', 40),
    (5, 'Standard', 2, 'Double', 150),
    (5, 'Deluxe', 3, 'Queen', 200),
    (5, 'Suite', 3, 'King', 400),
    (5, 'Studio', 1, 'Single', 100),
    (9, 'Standard', 2, 'Double', 140),
    (9, 'Deluxe', 3, 'Queen', 250),
    (9, 'Suite', 3, 'King', 360),
    (14, 'Standard', 2, 'Double', 100),
    (14, 'Suite', 3, 'King', 150),
    (14 ,'Studio', 1, 'Single', 70)
;

INSERT INTO
    room(hotel_id, room_spec_id, number, floor, is_active)
VALUES
    (1, 1, 101, 1, true),
    (1, 2, 302, 3, true),
    (1, 2, 303, 3, true),
    (1, 3, 505, 5, true),
    (1, 4, 404, 4, true),
    (1, 4, 407, 4, true),
    (5, 5, 300, 3, true),
    (5, 5, 310, 3, true),
    (5, 6, 710, 7, true),
    (5, 6, 702, 7, true),
    (5, 7, 211, 2, true),
    (5, 7, 213, 2, false),
    (5, 8, 344, 3, true),
    (5, 8, 333, 3, false),
    (9, 9, 877, 8, true),
    (9, 10, 407, 4, true),
    (9, 9, 897, 8, true),
    (9, 10, 423, 4, true)
;


INSERT INTO feature ("name",description)
    VALUES ('Iron', 'To iron clothes'),
           ('Kettle','To heat water'),
           ('Dryer','To dry hair'),
           ('Toiletries','Soap, shampoo, conditioner, body wash'),
           ('Bath Robe','To keep dry'),
           ('Wifi','Internet Access'),
           ('Mini Fridge','Because its awesome'),
           ('TV','Watch Netflix')
;

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
           (1,DATE '2022-09-11',DATE '2022-09-15',TRUE,9),
           (2,DATE '2022-10-17',DATE '2022-10-18',TRUE,1),
           (2,DATE '2022-08-12',DATE '2022-08-18',TRUE,1),
           (3,DATE '2022-06-05',DATE '2022-06-10',TRUE,1)

;


INSERT INTO room_booking(booking_id,room_id,is_active,main_guest,no_of_guests)
    VALUES
           (1,15,TRUE,'Bertrand Russell',2),
           --(1,16,TRUE,'Arman Arethna',2),
           (2,2,TRUE,'Elvis Presley',1),
           (2,2,TRUE,'John Lennon',2),
           (3,3,TRUE,'AR Rahman',2)
;
