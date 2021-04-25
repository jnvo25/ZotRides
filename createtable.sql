DROP DATABASE IF EXISTS zotrides;
CREATE DATABASE zotrides;
USE zotrides;

DROP TABLE IF EXISTS Cars;
CREATE TABLE Cars(
        id  varchar(10) NOT NULL PRIMARY KEY,
        model varchar(100) NOT NULL,
        make varchar(100) NOT NULL,
        year integer NOT NULL
);

DROP TABLE IF EXISTS CarPrices;
CREATE TABLE CarPrices(
        carID  varchar(10) NOT NULL PRIMARY KEY,
        price integer NOT NULL,
        FOREIGN KEY(carID) REFERENCES Cars(id)
);

/*
DROP TABLE IF EXISTS Images;
CREATE TABLE Images(
        make  varchar(100) NOT NULL,
        category varchar(100) NOT NULL,
        imageurl varchar(100) NOT NULL,
        PRIMARY KEY(make, category)
);
*/

DROP TABLE IF EXISTS Category;
CREATE TABLE Category(
        id  integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
        name varchar(100) NOT NULL
);

DROP TABLE IF EXISTS PickupLocation;
CREATE TABLE PickupLocation(
        id  varchar(10) NOT NULL PRIMARY KEY,
        address varchar(200) NOT NULL,
		phoneNumber varchar(20) DEFAULT "" -- NOT REQUIRED, does this default string work?
);

DROP TABLE IF EXISTS CreditCards;
CREATE TABLE CreditCards(
        id  varchar(20) NOT NULL PRIMARY KEY,
        firstName varchar(50) NOT NULL,
        lastName varchar(50) NOT NULL,
        expiration date NOT NULL
);

DROP TABLE IF EXISTS Customers;
CREATE TABLE Customers(
        id  integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
        firstName varchar(50) NOT NULL,
        lastName varchar(50) NOT NULL,
        ccID varchar(20) NOT NULL,
        address varchar(200) NOT NULL,
        email varchar(50) NOT NULL,
        password varchar(20) NOT NULL,
        FOREIGN KEY(ccID) REFERENCES CreditCards(id)
);

DROP TABLE IF EXISTS Reservations; 
CREATE TABLE Reservations(
        id  integer NOT NULL AUTO_INCREMENT PRIMARY KEY,
        startDate date NOT NULL,
        endDate date NOT NULL,
        customerID integer NOT NULL,
        carID varchar(10) NOT NULL, 
        saleDate date NOT NULL,
        saleID integer NOT NULL,
        FOREIGN KEY(customerID) REFERENCES Customers(id),
        FOREIGN KEY(carID) REFERENCES Cars(id)
);

DROP TABLE IF EXISTS Ratings;       -- not every car should have to have a rating?
CREATE TABLE Ratings(
        carID varchar(10) NOT NULL PRIMARY KEY,
        rating float NOT NULL,
        numVotes integer NOT NULL,              
        FOREIGN KEY(carID) REFERENCES Cars(id) 
);

DROP TABLE IF EXISTS category_of_car;
CREATE TABLE category_of_car(
        carID varchar(10) NOT NULL PRIMARY KEY, 
        categoryID integer NOT NULL,
        FOREIGN KEY(carID) REFERENCES Cars(id),
        FOREIGN KEY(categoryID) REFERENCES Category(id)
);

/*
ALTER TABLE Cars
ADD FOREIGN KEY (id) REFERENCES category_of_car(carID); */ -- added this into data entry SQL file

DROP TABLE IF EXISTS pickup_car_from;
CREATE TABLE pickup_car_from(
		carID varchar(10) NOT NULL,
        pickupLocationID varchar(10) NOT NULL,
        FOREIGN KEY(carID) REFERENCES Cars(id),
        FOREIGN KEY(pickupLocationID) REFERENCES PickupLocation(id),
        PRIMARY KEY(carID, pickupLocationID)
);

/*
ALTER TABLE Cars
ADD FOREIGN KEY (id) REFERENCES pickup_car_from(carID); */ -- added this into data entry SQL