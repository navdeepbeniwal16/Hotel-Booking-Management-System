DROP TABLE IF EXISTS test_table CASCADE;

DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS district CASCADE;

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS hotelier CASCADE;
DROP TABLE IF EXISTS system_admin CASCADE;

DROP TABLE IF EXISTS hotel_group CASCADE;
DROP TABLE IF EXISTS hg_hotelier_list CASCADE;
DROP TABLE IF EXISTS hotel_group_hotelier CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;

DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS room_spec CASCADE;
DROP TABLE IF EXISTS room_type CASCADE;
DROP TABLE IF EXISTS bed_type CASCADE;

DROP TABLE IF EXISTS amenities CASCADE;
DROP TABLE IF EXISTS amenity CASCADE;
DROP TABLE IF EXISTS room_amenities CASCADE;
DROP TABLE IF EXISTS room_amenity CASCADE;

DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS room_booking CASCADE;

DROP TABLE IF EXISTS guest CASCADE;
DROP TABLE IF EXISTS booking_guest CASCADE;


CREATE TABLE DISTRICT(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(3) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ADDRESS(
    id INT GENERATED ALWAYS AS IDENTITY,
    line_1 VARCHAR(50) NOT NULL,
    line_2 VARCHAR(50),
    district INT NOT NULL,
    city VARCHAR(40) NOT NULL,
    postcode INT NOT NULL,
    FOREIGN KEY (district) REFERENCES DISTRICT(id),
    PRIMARY KEY (id)
);


CREATE TABLE APP_USER(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    password VARCHAR(50) NOT NULL,
    role INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE SYSTEM_ADMIN( 
    id INT GENERATED ALWAYS AS IDENTITY,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES APP_USER(id)
);

CREATE TABLE HOTELIER( 
    id INT GENERATED ALWAYS AS IDENTITY,
    user_id INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES APP_USER(id)
);

CREATE TABLE CUSTOMER( 
    customer_id INT GENERATED ALWAYS AS IDENTITY, 
    user_id INT NOT NULL,
    address INT NOT NULL,
    contact VARCHAR(13) NOT NULL,
    age INT NOT NULL,
    PRIMARY KEY (customer_id), 
    FOREIGN KEY (user_id) REFERENCES APP_USER(id),
    FOREIGN KEY (address) REFERENCES ADDRESS(id)
);

CREATE TABLE HOTEL_GROUP( 
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    address INT NOT NULL,
    business_details VARCHAR(50) NOT NULL,
    FOREIGN KEY (address) REFERENCES ADDRESS(id),
    PRIMARY KEY (id)
);

CREATE TABLE HOTEL_GROUP_HOTELIER(
    id INT GENERATED ALWAYS AS IDENTITY,
    hotelier_id INT NOT NULL, 
    hotel_group_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (hotelier_id) REFERENCES HOTELIER(id),
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(id)
);

CREATE TABLE HOTEL( 
    id INT GENERATED ALWAYS AS IDENTITY,
    hotel_group_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL CHECK (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    contact VARCHAR(50) NOT NULL,
    address INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (address) REFERENCES ADDRESS(id),
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(id)
);

CREATE TABLE BED_TYPE(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(12) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ROOM_TYPE(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ROOM_SPEC( 
    id INT GENERATED ALWAYS AS IDENTITY,
    hotel_id INT NOT NULL,
    type INT NOT NULL,
    max_occupancy INT NOT NULL,
    bed_type INT NOT NULL,
    view VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(id),
    FOREIGN KEY (bed_type) REFERENCES BED_TYPE(id),
    FOREIGN KEY (type) REFERENCES ROOM_TYPE(id)
);

CREATE TABLE AMENITY(
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ROOM_AMENITY(
    id INT GENERATED ALWAYS AS IDENTITY,
    amenity_id INT NOT NULL,
    room_spec_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_spec_id) REFERENCES ROOM_SPEC(id),
    FOREIGN KEY (amenity_id) REFERENCES AMENITY(id)
);

CREATE TABLE ROOM( 
    id INT GENERATED ALWAYS AS IDENTITY,
    room_spec_id INT NOT NULL,
    number INT NOT NULL,
    floor INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_spec_id) REFERENCES ROOM_SPEC(id)
);

CREATE TABLE BOOKING( 
    id INT GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id)
);

CREATE TABLE ROOM_BOOKING( 
    id INT GENERATED ALWAYS AS IDENTITY,
    booking_id INT NOT NULL,
    room_id INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (booking_id) REFERENCES BOOKING(id),
    FOREIGN KEY (room_id) REFERENCES ROOM(id)
);

CREATE TABLE GUEST( 
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE BOOKING_GUEST( 
    id INT GENERATED ALWAYS AS IDENTITY,
    booking_id INT NOT NULL,
    guest_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (booking_id) REFERENCES BOOKING(id),
    FOREIGN KEY (guest_id) REFERENCES GUEST(id)
);