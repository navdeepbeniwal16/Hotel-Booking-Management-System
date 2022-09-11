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
    ('92 Lonsdale Street', NULL, 1, 'Melbourne', 3000)
;

INSERT INTO
    phone(country, area, number)
VALUES
    (61, 4, 96623900)
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
    hotel(hotel_group_id, name, email, address, phone)
VALUES
    (1, 'Melbourne Marriott Hotel', 'melbourne@marriott.com', 2, 1);

INSERT INTO
    bed_type(name)
VALUES
    ('Single'), ('Double'), ('Queen'), ('King');

INSERT INTO
    room_type(name)
VALUES
    ('Standard'), ('Deluxe'), ('Premium'), ('Penthouse');

INSERT INTO
    room_spec(hotel_id, type, max_occupancy, bed_type, view)
VALUES
    (1, 1, 2, 1, 'Street view'),
    (1, 2, 2, 3, 'Street view'),
    (1, 3, 3, 4, 'Above street')
;

INSERT INTO
    room(hotel_id, room_spec_id, floor, number, is_active)
VALUES
    (1, 1, 0, 1, true),
    (1, 1, 0, 2, true),
    (1, 2, 1, 3, true)
;