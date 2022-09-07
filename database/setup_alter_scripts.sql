/*Add email constraint for hotel*/
ALTER TABLE HOTEL ADD CONSTRAINT email_check CHECK (hotel_email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$');

/*Drop column user_mail from users table*/
ALTER TABLE USERS DROP COLUMN user_mail;

/*Add email column for users*/
ALTER TABLE USERS ADD user_email VARCHAR(50);

/*Add email constraint for hotel*/
ALTER TABLE USERS ADD CONSTRAINT users_email_check CHECK (user_email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$');

/*Drop column htoel_address from users table*/
ALTER TABLE HOTEL DROP COLUMN htoel_address;

/*Add hotel_address column for users*/
ALTER TABLE HOTEL ADD hotel_address VARCHAR(50);
