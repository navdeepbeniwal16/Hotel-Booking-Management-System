DROP TABLE IF EXISTS test_table CASCADE;

DROP TABLE IF EXISTS district CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS system_admin CASCADE;
DROP TABLE IF EXISTS hotelier CASCADE;
DROP TABLE IF EXISTS hotel_group CASCADE;
DROP TABLE IF EXISTS hotel_group_hotelier CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;
DROP TABLE IF EXISTS room_spec CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS feature CASCADE;
DROP TABLE IF EXISTS room_spec_feature CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS room_booking CASCADE;
DROP TABLE IF EXISTS room_type CASCADE;
DROP TABLE IF EXISTS bed_type CASCADE;
DROP TABLE IF EXISTS phone CASCADE;
DROP TABLE IF EXISTS business_detail_type CASCADE;
DROP TABLE IF EXISTS hotel_group_business_detail CASCADE;
DROP TABLE IF EXISTS hotel_amenity CASCADE;
DROP TABLE IF EXISTS amenity CASCADE;
DROP TABLE IF EXISTS room_feature CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS roll CASCADE;


CREATE TABLE ROLES(
    id INT UNIQUE,
    name VARCHAR(10) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

INSERT INTO
    ROLES(id, name)
VALUES
    (1, 'Admin'),
    (2, 'Hotelier'),
    (3, 'Customer');


CREATE TABLE DISTRICT(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(3) NOT NULL UNIQUE ,
    PRIMARY KEY (id)
);

CREATE TABLE ADDRESS (
    id INT GENERATED ALWAYS AS IDENTITY,
    line_1 VARCHAR(50) NOT NULL,
    line_2 VARCHAR(50),
    district INT NOT NULL,
    city VARCHAR(40) NOT NULL,
    postcode INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (district) REFERENCES DISTRICT(id),
    PRIMARY KEY (id)
);

CREATE TABLE APP_USER (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50),
    email VARCHAR(50) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    address INT,
    role INT NOT NULL,
    contact VARCHAR(13),
    age INT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (address) REFERENCES address
);

CREATE TABLE HOTEL_GROUP (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL UNIQUE,
    address INT NOT NULL,
    phone VARCHAR(13) NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    FOREIGN KEY (address) REFERENCES ADDRESS(id),
    PRIMARY KEY (id)
);

CREATE TABLE HOTEL_GROUP_HOTELIER (
    id INT GENERATED ALWAYS AS IDENTITY,
    hotelier_id INT NOT NULL UNIQUE ,
    hotel_group_id INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (hotelier_id) REFERENCES APP_USER(id),
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(id)
);

CREATE TABLE HOTEL (
    id INT GENERATED ALWAYS AS IDENTITY,
    hotel_group_id INT NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    address INT NOT NULL,
    contact VARCHAR(13) NULL,
    city VARCHAR(50) NOT NULL,
    pin_code INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (address) REFERENCES ADDRESS(id),
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(id)
);

CREATE TABLE ROOM (
    id INT GENERATED ALWAYS AS IDENTITY,
    hotel_id INT NOT NULL,
    type VARCHAR(50),
    max_occupancy INT NOT NULL,
    bed_type VARCHAR(50) NOT NULL,
    room_price INTEGER NOT NULL,
    number INT NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);


CREATE TABLE BOOKING (
    id INT GENERATED ALWAYS AS IDENTITY,
    hotel_id INT NOT NULL,
    customer_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES APP_USER(id),
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id)
);

CREATE TABLE ROOM_BOOKING (
    id INT GENERATED ALWAYS AS IDENTITY,
    booking_id INT NOT NULL,
    room_id INT NOT NULL,
    main_guest VARCHAR(50) NOT NULL,
    no_of_guests INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (booking_id) REFERENCES BOOKING(id),
    FOREIGN KEY (room_id) REFERENCES ROOM(id)
);

CREATE OR REPLACE FUNCTION check_availability(room_id INTEGER, booking_id INTEGER)
RETURNS BOOLEAN AS
$$
BEGIN
    LOCK booking IN SHARE MODE;
    LOCK room_booking IN SHARE MODE;
    RETURN(
        WITH check_booking AS (
        SELECT start_date, end_date
        FROM booking
        WHERE id = $2
        LIMIT 1
    )
    SELECT (COUNT(*) = 0)
    FROM room_booking AS rb
    JOIN booking AS b
    ON rb.booking_id = b.id
    WHERE rb.room_id = $1
    AND daterange(b.start_date, b.end_date, '[]') @> (SELECT start_date FROM check_booking)
    AND daterange(b.start_date, b.end_date, '[]') @> (SELECT end_date FROM check_booking)
        );
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION valid_room_booking(room_id INTEGER, booking_id INTEGER)
RETURNS BOOLEAN AS
$$
BEGIN
    LOCK room_booking IN SHARE MODE;
    LOCK booking IN SHARE MODE;
    LOCK hotel IN SHARE MODE;
    RETURN(
        SELECT
            CASE WHEN b.hotel_id = h.id THEN TRUE ELSE FALSE END
        FROM room AS r
        LEFT JOIN hotel AS h
            ON h.id = r.hotel_id
        LEFT JOIN booking AS b
            ON b.id = $2
        WHERE r.id = $1
    );
END
$$
LANGUAGE plpgsql;

ALTER TABLE room_booking
ADD CONSTRAINT room_available
CHECK (check_availability(room_id, booking_id));

ALTER TABLE room_booking
ADD CONSTRAINT room_valid
CHECK (valid_room_booking(room_id, booking_id));
