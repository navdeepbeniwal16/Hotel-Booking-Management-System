CREATE TABLE USERS( 
    users_id INT GENERATED ALWAYS AS IDENTITY, 
    user_mail VARCHAR(50) NOT NULL, 
    user_pw VARCHAR(50) NOT NULL, 
    user_role INT NOT NULL, 
    PRIMARY KEY (users_id) 
);

CREATE TABLE SYSTEM_ADMIN( 
    admin_id INT GENERATED ALWAYS AS IDENTITY, 
    users_id INT NOT NULL, 
    PRIMARY KEY (admin_id), 
    FOREIGN KEY (users_id) REFERENCES USERS(users_id) 
);

CREATE TABLE HOTELIER( 
    hotelier_id INT GENERATED ALWAYS AS IDENTITY, 
    users_id INT NOT NULL, 
    hotelier_name VARCHAR(50) NOT NULL,
    isActive BOOLEAN NOT NULL,
    PRIMARY KEY (hotelier_id), 
    FOREIGN KEY (users_id) REFERENCES USERS(users_id) 
);

CREATE TABLE CUSTOMER( 
    customer_id INT GENERATED ALWAYS AS IDENTITY, 
    users_id INT NOT NULL, 
    customer_name VARCHAR(50) NOT NULL,
    customer_address VARCHAR(200) NOT NULL,
    customer_contact VARCHAR(13) NOT NULL,
    customer_age INT NOT NULL,
    PRIMARY KEY (customer_id), 
    FOREIGN KEY (users_id) REFERENCES USERS(users_id) 
);

CREATE TABLE HOTEL_GROUP( 
    hotel_group_id INT GENERATED ALWAYS AS IDENTITY, 
    hg_name VARCHAR(50) NOT NULL,
    hg_res_address VARCHAR(50) NOT NULL,
    hg_business_details VARCHAR(50) NOT NULL,
    PRIMARY KEY (hotel_group_id)
);

CREATE TABLE HG_HOTELIER_LIST( 
    hg_hotelier_id INT GENERATED ALWAYS AS IDENTITY, 
    hotelier_id INT NOT NULL, 
    hotel_group_id INT NOT NULL,
    PRIMARY KEY (hg_hotelier_id), 
    FOREIGN KEY (hotelier_id) REFERENCES HOTELIER(hotelier_id), 
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(hotel_group_id)
);


CREATE TABLE HOTEL( 
    hotel_id INT GENERATED ALWAYS AS IDENTITY, 
    hotel_group_id INT NOT NULL,
    hotel_name VARCHAR(50) NOT NULL,
    hotel_email VARCHAR(50) NOT NULL,
    hotel_contact VARCHAR(50) NOT NULL,
    htoel_address VARCHAR(50) NOT NULL,
    PRIMARY KEY (hotel_id), 
    FOREIGN KEY (hotel_group_id) REFERENCES HOTEL_GROUP(hotel_group_id) 
);

CREATE TABLE ROOM_SPEC( 
    room_spec_id INT GENERATED ALWAYS AS IDENTITY, 
    hotel_id INT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    room_max_occupancy INT NOT NULL,
    room_bed_type VARCHAR(50) NOT NULL,
    room_view VARCHAR(50) NOT NULL,
    PRIMARY KEY (room_spec_id), 
    FOREIGN KEY (hotel_id) REFERENCES HOTEL(hotel_id) 
);

CREATE TABLE AMENITIES( 
    amenity_id INT GENERATED ALWAYS AS IDENTITY, 
    amenity_name VARCHAR(50) NOT NULL,
    amenity_desc VARCHAR(50) NOT NULL,
    PRIMARY KEY (amenity_id)
);

CREATE TABLE ROOM_AMENITIES( 
    room_amenities_id INT GENERATED ALWAYS AS IDENTITY, 
    amenity_id INT NOT NULL,
    room_spec_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (room_amenities_id), 
    FOREIGN KEY (room_spec_id) REFERENCES ROOM_SPEC(room_spec_id),
    FOREIGN KEY (amenity_id) REFERENCES AMENITIES(amenity_id)
);

CREATE TABLE ROOM( 
    room_id INT GENERATED ALWAYS AS IDENTITY, 
    room_spec_id INT NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    room_floor INT NOT NULL,
    isActive BOOLEAN NOT NULL,
    PRIMARY KEY (room_id), 
    FOREIGN KEY (room_spec_id) REFERENCES ROOM_SPEC(room_spec_id)
);

CREATE TABLE BOOKING( 
    booking_id INT GENERATED ALWAYS AS IDENTITY, 
    customer_id INT NOT NULL,
    booking_start_date DATE NOT NULL,
    booking_end_date DATE NOT NULL,
    isActive BOOLEAN NOT NULL,
    PRIMARY KEY (booking_id), 
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id)
);

CREATE TABLE ROOM_BOOKING( 
    room_book_id INT GENERATED ALWAYS AS IDENTITY, 
    booking_id INT NOT NULL,
    room_id INT NOT NULL,
    isActive BOOLEAN NOT NULL,
    PRIMARY KEY (room_book_id), 
    FOREIGN KEY (booking_id) REFERENCES BOOKING(booking_id),
    FOREIGN KEY (room_id) REFERENCES ROOM(room_id)
);

CREATE TABLE GUEST( 
    guest_id INT GENERATED ALWAYS AS IDENTITY, 
    guest_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (guest_id)
);

CREATE TABLE BOOKING_GUEST( 
    room_guest_id INT GENERATED ALWAYS AS IDENTITY, 
    booking_id INT NOT NULL,
    guest_id INT NOT NULL,
    PRIMARY KEY (room_guest_id), 
    FOREIGN KEY (booking_id) REFERENCES BOOKING(booking_id),
    FOREIGN KEY (guest_id) REFERENCES GUEST(guest_id)
);