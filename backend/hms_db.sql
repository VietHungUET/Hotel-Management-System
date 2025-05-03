-- Database: hmsdb
-- PostgreSQL-compatible script

-- Drop tables if they exist
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS guest CASCADE;
DROP TABLE IF EXISTS hotel CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS room_type CASCADE;
DROP TABLE IF EXISTS staff CASCADE;
DROP TABLE IF EXISTS user_account CASCADE;
DROP TABLE IF EXISTS user_session CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create table: booking
CREATE TABLE booking (
  booking_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  checkin_date timestamp(6) DEFAULT NULL,
  checkout_date timestamp(6) DEFAULT NULL,
  guest_id int DEFAULT NULL,
  room_number int NOT NULL,
  total_price double precision DEFAULT NULL,
  guest_notes varchar(255) DEFAULT NULL,
  PRIMARY KEY (booking_id)
);

-- Insert data into booking
INSERT INTO booking (booking_id, checkin_date, checkout_date, guest_id, room_number, total_price, guest_notes)
OVERRIDING SYSTEM VALUE
VALUES
(1, '2024-05-01 07:00:00.000000', '2024-05-03 07:00:00.000000', 1, 1, 200, NULL),
(2, '2024-06-10 07:00:00.000000', '2024-06-15 07:00:00.000000', 2, 3, 750, NULL),
(3, '2024-07-20 07:00:00.000000', '2024-07-25 07:00:00.000000', 3, 5, 1250, NULL),
(4, '2024-08-01 00:00:00.000000', '2024-08-05 00:00:00.000000', 4, 1, 400, NULL),
(5, '2024-08-10 00:00:00.000000', '2024-08-15 00:00:00.000000', 5, 2, 800, NULL),
(6, '2024-09-01 00:00:00.000000', '2024-09-03 00:00:00.000000', 6, 3, 300, NULL),
(7, '2024-09-10 00:00:00.000000', '2024-09-15 00:00:00.000000', 7, 4, 1000, NULL),
(8, '2024-10-01 00:00:00.000000', '2024-10-05 00:00:00.000000', 8, 5, 500, NULL),
(9, '2024-10-10 00:00:00.000000', '2024-10-15 00:00:00.000000', 9, 6, 900, NULL),
(10, '2024-11-01 00:00:00.000000', '2024-11-03 00:00:00.000000', 10, 7, 350, NULL),
(11, '2024-11-10 00:00:00.000000', '2024-11-15 00:00:00.000000', 11, 8, 1100, NULL),
(12, '2024-12-01 00:00:00.000000', '2024-12-05 00:00:00.000000', 12, 9, 600, NULL),
(13, '2024-12-10 00:00:00.000000', '2024-12-15 00:00:00.000000', 13, 10, 950, NULL),
(14, '2025-01-01 00:00:00.000000', '2025-01-03 00:00:00.000000', 14, 11, 400, NULL),
(15, '2025-01-10 00:00:00.000000', '2025-01-15 00:00:00.000000', 15, 12, 800, NULL),
(16, '2025-02-01 00:00:00.000000', '2025-02-05 00:00:00.000000', 16, 13, 300, NULL),
(17, '2025-02-10 00:00:00.000000', '2025-02-15 00:00:00.000000', 17, 14, 1000, NULL),
(18, '2025-03-01 00:00:00.000000', '2025-03-03 00:00:00.000000', 18, 15, 500, NULL),
(19, '2025-03-10 00:00:00.000000', '2025-03-15 00:00:00.000000', 19, 16, 900, NULL),
(20, '2025-04-01 00:00:00.000000', '2025-04-05 00:00:00.000000', 20, 17, 350, NULL),
(21, '2025-05-01 00:00:00.000000', '2025-05-03 00:00:00.000000', 22, 20, 600, NULL);
-- Set sequence value for booking
SELECT setval(pg_get_serial_sequence('booking', 'booking_id'), 22);

-- Create table: guest
CREATE TABLE guest (
  guest_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  address varchar(255) DEFAULT NULL,
  date_of_birth timestamp(6) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  PRIMARY KEY (guest_id)
);

-- Insert data into guest
INSERT INTO guest (guest_id, address, date_of_birth, email, first_name, last_name, phone)
OVERRIDING SYSTEM VALUE
VALUES
(1, '123 Oak St, Springfield', '1987-03-25 07:00:00.000000', 'alice.johnson@example.com', 'Alice', 'Johnson', '111-222-3333'),
(2, '456 Maple Ave, Hilltown', '1995-10-12 07:00:00.000000', 'bob.smith@example.com', 'Bob', 'Smith', '444-555-6666'),
(3, '789 Pine St, Lakeside', '1980-08-05 07:00:00.000000', 'emma.williams@example.com', 'Emma', 'Williams', '777-888-9999'),
(4, '321 Elm St, Riverside', '1990-04-15 00:00:00.000000', 'john.doe@example.com', 'John', 'Doe', '333-444-5555'),
(5, '555 Pine Rd, Hilltop', '1985-12-10 00:00:00.000000', 'susan.jones@example.com', 'Susan', 'Jones', '666-777-8888'),
(6, '777 Oak Ave, Seaside', '1978-07-20 00:00:00.000000', 'michael.brown@example.com', 'Michael', 'Brown', '999-000-1111'),
(7, '888 Maple St, Lakeside', '1993-06-25 00:00:00.000000', 'laura.white@example.com', 'Laura', 'White', '222-333-4444'),
(8, '444 Cedar Dr, Mountainville', '1982-03-05 00:00:00.000000', 'chris.wilson@example.com', 'Chris', 'Wilson', '555-666-7777'),
(9, '999 Elm St, Hilltown', '1997-09-12 00:00:00.000000', 'jessica.moore@example.com', 'Jessica', 'Moore', '888-999-0000'),
(10, '222 Birch Ln, Valleyview', '1989-11-30 00:00:00.000000', 'david.anderson@example.com', 'David', 'Anderson', '111-222-3333'),
(11, '777 Ash St, Sunset', '1984-02-18 00:00:00.000000', 'amanda.thomas@example.com', 'Amanda', 'Thomas', '444-555-6666'),
(12, '555 Oak Rd, Riverside', '1980-08-25 00:00:00.000000', 'matthew.jackson@example.com', 'Matthew', 'Jackson', '777-888-9999'),
(13, '111 Pine Ave, Mountain View', '1995-01-07 00:00:00.000000', 'jennifer.martin@example.com', 'Jennifer', 'Martin', '222-333-4444'),
(14, '666 Cedar Ln, Bayville', '1983-05-30 00:00:00.000000', 'kevin.roberts@example.com', 'Kevin', 'Roberts', '555-666-7777'),
(15, '888 Elm St, Lakeside', '1987-10-20 00:00:00.000000', 'emily.clark@example.com', 'Emily', 'Clark', '888-999-0000'),
(16, '333 Maple Ave, Riverfront', '1990-11-15 00:00:00.000000', 'mark.hall@example.com', 'Mark', 'Hall', '111-222-3333'),
(17, '444 Oak Dr, Meadowview', '1982-06-12 00:00:00.000000', 'nicole.lewis@example.com', 'Nicole', 'Lewis', '444-555-6666'),
(18, '777 Pine St, Hilltop', '1978-12-05 00:00:00.000000', 'justin.lee@example.com', 'Justin', 'Lee', '777-888-9999'),
(19, '222 Elm Ave, Lakeside', '1993-09-08 00:00:00.000000', 'sarah.robertson@example.com', 'Sarah', 'Robertson', '222-333-4444'),
(20, '555 Birch Ln, Riverfront', '1985-02-28 00:00:00.000000', 'ryan.rogers@example.com', 'Ryan', 'Rogers', '555-666-7777'),
(21, '111 Cedar Rd, Valleyview', '1997-07-22 00:00:00.000000', 'rachel.collins@example.com', 'Rachel', 'Collins', '888-999-0000'),
(22, '999 Oak St, Hilltown', '1989-04-10 00:00:00.000000', 'jason.ward@example.com', 'Jason', 'Ward', '111-222-3333'),
(23, '777 Maple Ave, Lakeside', '1984-11-20 00:00:00.000000', 'amanda.scott@example.com', 'Amanda', 'Scott', '444-555-6666'),
(24, '222 Cedar Dr, Riverfront', '1995-06-18 00:00:00.000000', 'patrick.phillips@example.com', 'Patrick', 'Phillips', '777-888-9999'),
(25, '555 Elm St, Mountain View', '1982-10-05 00:00:00.000000', 'linda.bailey@example.com', 'Linda', 'Bailey', '222-333-4444'),
(26, '111 Pine Rd, Meadowview', '1990-01-15 00:00:00.000000', 'brian.nelson@example.com', 'Brian', 'Nelson', '555-666-7777'),
(27, '888 Oak Ln, Bayville', '1978-06-30 00:00:00.000000', 'kelly.wright@example.com', 'Kelly', 'Wright', '888-999-0000'),
(28, '333 Maple Dr, Hilltop', '1987-12-08 00:00:00.000000', 'jacob.ross@example.com', 'Jacob', 'Ross', '111-222-3333'),
(29, '444 Pine Ave, Valleyview', '1993-03-25 00:00:00.000000', 'natalie.long@example.com', 'Natalie', 'Long', '444-555-6666'),
(30, '777 Elm St, Lakeside', '1978-08-20 00:00:00.000000', 'tyler.harris@example.com', 'Tyler', 'Harris', '777-888-9999'),
(31, '222 Cedar Rd, Hilltown', '1998-05-12 00:00:00.000000', 'julia.morris@example.com', 'Julia', 'Morris', '222-333-4444'),
(32, '555 Birch Ave, Riverfront', '1985-01-07 00:00:00.000000', 'nathan.howard@example.com', 'Nathan', 'Howard', '555-666-7777'),
(33, '111 Oak Rd, Valleyview', '1997-09-18 00:00:00.000000', 'olivia.wood@example.com', 'Olivia', 'Wood', '888-999-0000'),
(34, '999 Elm Ave, Lakeside', '1989-11-30 00:00:00.000000', 'megan.cook@example.com', 'Megan', 'Cook', '111-222-3333');

-- Set sequence value for guest
SELECT setval(pg_get_serial_sequence('guest', 'guest_id'), 35);

-- Create table: hotel
CREATE TABLE hotel (
  hotel_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  address varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  checkin_time varchar(255) DEFAULT NULL,
  checkout_time varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  star int DEFAULT NULL,
  PRIMARY KEY (hotel_id)
);

-- Insert data into hotel
INSERT INTO hotel (hotel_id, address, name, phone, checkin_time, checkout_time, email, star)
OVERRIDING SYSTEM VALUE
VALUES
(1, '789 Broadway Ave, Metropolis', 'Grand Plaza Hotel', '123-456-7890', '14:00', '12:00', 'info@grandplazahotel.com', 5),
(2, '101 Riverfront Dr, Riverside', 'Riverside Lodge', '987-654-3210', '15:00', '11:00', 'reservations@riversidelodge.com', 4),
(3, '456 Mountain Rd, Hilltop', 'Mountain View Inn', '555-123-4567', '13:00', '10:00', 'info@mountainviewinn.com', 3),
(5, '123 Main St, Lakeside', 'Lakeside Resort', '555-111-2222', '14:00', '12:00', 'info@lakesideresort.com', 4),
(6, '789 Ocean Blvd, Seaside', 'Seaside Inn', '444-333-2222', '15:00', '11:00', 'reservations@seasideinn.com', 3),
(7, '321 Forest Ave, Woodland', 'Woodland Retreat', '777-888-9999', '13:00', '10:00', 'info@woodlandretreat.com', 3),
(8, '555 Sunset Dr, Sunset', 'Sunset Sands', '666-555-4444', '14:30', '12:30', 'hello@sunsetsands.com', 4),
(9, '987 Lakeview Rd, Lakeside', 'Lakeview Lodge', '333-222-1111', '16:00', '11:00', 'welcome@lakeviewlodge.com', 4),
(10, '456 Hilltop Ave, Hilltown', 'Hilltown Hotel', '999-888-7777', '14:00', '12:00', 'info@hilltownhotel.com', 3),
(11, '777 Valley Dr, Valleyview', 'Valley Vista Inn', '222-333-4444', '15:00', '11:30', 'reservations@valleyvistainn.com', 3),
(12, '888 River Rd, Riverside', 'Riverside Resort', '111-222-3333', '13:00', '10:00', 'info@riversideresort.com', 4),
(13, '444 Summit Ave, Summit', 'Summit Suites', '777-666-5555', '12:00', '10:00', 'welcome@summitsuites.com', 5),
(14, '555 Forest Rd, Forestville', 'Forest View Hotel', '333-444-5555', '14:00', '12:00', 'info@forestviewhotel.com', 3),
(15, '222 Mountain View, Mountainville', 'Mountain Peak Lodge', '888-999-0000', '15:00', '11:00', 'reservations@mountainpeaklodge.com', 4),
(16, '123 Pine St, Pinewood', 'Pinewood Retreat', '555-666-7777', '13:00', '10:00', 'hello@pinewoodretreat.com', 3),
(17, '999 Maple Dr, Maplewood', 'Maplewood Manor', '222-111-0000', '14:00', '12:00', 'info@maplewoodmanor.com', 4),
(18, '777 Lakeside Ave, Lakeside', 'Lakeside Lagoon', '444-555-6666', '14:30', '12:30', 'welcome@lakesidelagoon.com', 4),
(19, '111 Bayview Blvd, Bayville', 'Bayview Beach Resort', '999-000-1111', '16:00', '11:00', 'info@bayviewbeachresort.com', 3),
(20, '222 Harbor Dr, Harborview', 'Harborview Haven', '666-777-8888', '15:00', '11:30', 'reservations@harborviewhaven.com', 3),
(21, '333 Oceanfront Rd, Oceanview', 'Oceanview Oasis', '777-888-9999', '13:00', '10:00', 'info@oceanviewoasis.com', 4),
(22, '777 Hillside Ave, Hillside', 'Hillside Heights', '444-333-2222', '12:00', '10:00', 'hello@hillsideheights.com', 5),
(23, '888 Sunrise Dr, Sunrise', 'Sunrise Sanctuary', '111-222-3333', '14:00', '12:00', 'info@sunrisesanctuary.com', 4),
(24, '555 Meadow Ln, Meadowview', 'Meadowview Mansion', '666-555-4444', '15:00', '11:00', 'reservations@meadowviewmansion.com', 5);

-- Set sequence value for hotel
SELECT setval(pg_get_serial_sequence('hotel', 'hotel_id'), 25);

-- Create table: payment
CREATE TABLE payment (
  payment_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  amount double precision DEFAULT NULL,
  booking_id int DEFAULT NULL,
  payment_date varchar(255) DEFAULT NULL,
  payment_method varchar(255) DEFAULT NULL,
  PRIMARY KEY (payment_id)
);

-- Insert data into payment (corrected bookingid values)
INSERT INTO payment (payment_id, amount, booking_id, payment_date, payment_method)
OVERRIDING SYSTEM VALUE
VALUES
(1, 200, 1, '2024-05-01 00:00:00.000000', 'Credit Card'),
(2, 750, 2, '2024-06-10 00:00:00.000000', 'PayPal'),
(3, 1200, 3, '2024-07-20 00:00:00.000000', 'Credit Card'),
(4, 300, 4, '2024-08-04 00:00:00.000000', 'Credit Card'),
(5, 500, 5, '2024-05-20 00:00:00.000000', 'PayPal'),
(6, 800, 6, '2024-06-01 00:00:00.000000', 'Credit Card'),
(7, 400, 7, '2024-06-05 00:00:00.000000', 'Cash'),
(8, 600, 8, '2024-06-10 00:00:00.000000', 'PayPal'),
(9, 200, 9, '2024-06-15 00:00:00.000000', 'Credit Card'),
(10, 1000, 10, '2024-06-20 00:00:00.000000', 'Credit Card'),
(11, 700, 11, '2024-07-01 00:00:00.000000', 'PayPal'),
(12, 900, 12, '2024-07-05 00:00:00.000000', 'Credit Card'),
(13, 1200, 13, '2024-07-10 00:00:00.000000', 'Credit Card'),
(14, 300, 14, '2024-07-15 00:00:00.000000', 'PayPal'),
(15, 500, 15, '2024-07-20 00:00:00.000000', 'Credit Card'),
(16, 800, 16, '2024-08-01 00:00:00.000000', 'Cash'),
(17, 400, 17, '2024-08-05 00:00:00.000000', 'PayPal'),
(18, 600, 18, '2024-08-10 00:00:00.000000', 'Credit Card'),
(19, 200, 19, '2024-08-15 00:00:00.000000', 'Credit Card'),
(20, 1000, 20, '2024-08-20 00:00:00.000000', 'PayPal'),
(21, 700, 21, '2024-09-01 00:00:00.000000', 'Credit Card');

-- Set sequence value for payment
SELECT setval(pg_get_serial_sequence('payment', 'payment_id'), 34);

-- Create table: room
CREATE TABLE room (
  room_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  room_number int NOT NULL,
  status varchar(255) DEFAULT NULL,
  hotel_id int DEFAULT NULL,
  type_id int DEFAULT NULL,
  notes varchar(255) DEFAULT NULL,
  PRIMARY KEY (room_number)
);

-- Insert data into room
INSERT INTO room (room_number, status, hotel_id, type_id, notes)
VALUES
(1, 'Available', 1, 1, NULL),
(10, 'Unavailable', 1, 2, NULL),
(11, 'Available', 2, 3, NULL),
(12, 'Unavailable', 1, 4, NULL),
(13, 'Available', 4, 5, NULL),
(14, 'Available', 5, 6, NULL),
(15, 'Available', 6, 7, NULL),
(16, 'Unavailable', 1, 8, NULL),
(17, 'Available', 8, 1, NULL),
(18, 'Unavailable', 9, 2, NULL),
(19, 'Available', 10, 3, NULL),
(2, 'Available', 1, 1, NULL),
(20, 'Unavailable', 11, 4, NULL),
(21, 'Available', 12, 5, NULL),
(22, 'Unavailable', 13, 6, NULL),
(23, 'Available', 14, 7, NULL),
(24, 'Unavailable', 15, 8, NULL),
(25, 'Available', 1, 1, NULL),
(26, 'Unavailable', 1, 2, NULL),
(27, 'Available', 1, 1, '503'),
(28, 'Available', 1, 7, '101'),
(29, 'Available', 1, 1, '520'),
(3, 'Unavailable', 2, 2, NULL),
(4, 'Unavailable', 2, 2, NULL),
(5, 'Available', 2, 3, NULL),
(6, 'Unavailable', 3, 4, NULL),
(7, 'Available', 1, 1, NULL),
(8, 'Available', 1, 4, NULL),
(9, 'Unavailable', 2, 4, NULL);

-- Create table: room_type
CREATE TABLE room_type (
  type_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  capacity int NOT NULL,
  price int NOT NULL,
  daily_rate double precision DEFAULT NULL,
  day_rate double precision DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  night_rate double precision DEFAULT NULL,
  overtime_pay double precision DEFAULT NULL,
  PRIMARY KEY (type_id)
);

-- Insert data into room_type
INSERT INTO room_type (type_id, capacity, description, name, price, daily_rate, day_rate, overtime_pay, night_rate)
OVERRIDING SYSTEM VALUE
VALUES
(1, '2', 'Standard room with basic amenities', 'Standard', 100, NULL, NULL, NULL, NULL),
(2, '2', 'Deluxe room with ocean view', 'Deluxe', 150, NULL, NULL, NULL, NULL),
(3, '4', 'Luxurious suite with living area', 'Suite', 250, NULL, NULL, NULL, NULL),
(4, '4', 'Spacious room suitable for families', 'Family Room', 200, NULL, NULL, NULL, NULL),
(5, '4', 'Has an area of 60 - 70m2, with beautiful views', 'Junior Suite', 250, NULL, NULL, NULL, NULL),
(6, '6', 'Has an area of up to 80m2, equipped with both standing and lying bathtubs', 'Executive Suite', 300, NULL, NULL, NULL, NULL),
(7, '4', 'One-story house, with unique architecture and beautiful view', 'Bungalow', 270, NULL, NULL, NULL, NULL),
(8, '3', 'Has better quality than Standard rooms', 'Superior', 120, NULL, NULL, NULL, NULL);

-- Set sequence value for roomtype
SELECT setval(pg_get_serial_sequence('room_type', 'type_id'), 9);

-- Create table: staff
CREATE TABLE staff (
  staff_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
  last_name varchar(255) DEFAULT NULL,
  date_birth varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  hire_date varchar(255) DEFAULT NULL,
  hotel_id int DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  position varchar(255) DEFAULT NULL,
  salary double precision DEFAULT NULL,
  PRIMARY KEY (staff_id)
);

-- Insert data into staff
INSERT INTO staff (staff_id, last_name, date_birth, email, first_name, hire_date, hotel_id, phone, position, salary)
OVERRIDING SYSTEM VALUE
VALUES
(1, 'Doe', '1980-05-10', 'john.doe@example.com', 'John', '2020-01-15', 1, '123-456-7890', 'Manager', 5000),
(2, 'Smith', '1990-08-20', 'jane.smith@example.com', 'Jane', '2021-03-20', 1, '987-654-3210', 'Receptionist', 3000),
(3, 'Johnson', '1985-11-25', 'michael.johnson@example.com', 'Michael', '2019-09-10', 2, '111-222-3333', 'Chef', 4000);

-- Set sequence value for staff
SELECT setval(pg_get_serial_sequence('staff', 'staff_id'), 6);

-- Create table: app_user (renamed from "user" to avoid reserved keyword)
CREATE TABLE users (
  hotelid int NOT NULL GENERATED ALWAYS AS IDENTITY,
  email varchar(255) DEFAULT NULL,
  full_name varchar(255) DEFAULT NULL,
  phone varchar(255) DEFAULT NULL,
  user_name varchar(255) DEFAULT NULL,
  user_password varchar(255) DEFAULT NULL,
  PRIMARY KEY (hotelid)
);

-- Create table: user_account
CREATE TABLE user_account (
  user_name varchar(255) NOT NULL,
  active boolean DEFAULT NULL,
  hotelid int DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(255) DEFAULT NULL,
  PRIMARY KEY (user_name)
);

---- Create table: user_session
--CREATE TABLE user_session (
--  session_id varchar(255) NOT NULL,
--  role varchar(255) DEFAULT NULL,
--  username varchar(255) DEFAULT NULL,
--  PRIMARY KEY (session_id)
--);

