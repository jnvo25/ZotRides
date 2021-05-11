use zotrides;


DROP PROCEDURE IF EXISTS add_car;
DELIMITER $$
CREATE PROCEDURE add_car(
	IN carMake varchar(100), carModel varchar(100), carYear integer, locAddress varchar(200), carCategory varchar(100),
    OUT message varchar(100)
)
BEGIN
	-- declare and assign helper variables for IDs
	DECLARE carID varchar(10);
    DECLARE pickupID varchar(10);
    DECLARE categoryID integer;
    SELECT MAX(id) INTO carID FROM Cars WHERE Cars.make = carMake AND Cars.model = carModel AND Cars.year = carYear;
    SELECT MAX(id) INTO pickupID FROM PickupLocation WHERE PickupLocation.address = locAddress;
    SELECT MAX(id) INTO categoryID FROM Category WHERE Category.name = carCategory;
    
	IF carID IS NOT NULL THEN
		SET message = 'Error : car already exists';
	ELSE
		-- get new car's id & increment helper table
		SELECT MAX(nextID) INTO carID FROM HelperCarsID;
        SET SQL_SAFE_UPDATES = 0;
        UPDATE HelperCarsID SET nextID = nextID + 1;
        SET SQL_SAFE_UPDATES = 1;
        
        -- add new car to Cars 
        INSERT INTO Cars VALUES(carID, carModel, carMake, carYear);
        
        -- add new location to PickupLocation (if applicable)
        IF pickupID IS NULL THEN
			SELECT MAX(nextID) INTO pickupID FROM HelperPickupID;
            SET SQL_SAFE_UPDATES = 0;
            UPDATE HelperPickupID SET nextID = nextID + 1;
            SET SQL_SAFE_UPDATES = 1;
			INSERT INTO PickupLocation(id, address) VALUES(pickupID, locAddress);
		END IF;
        
        -- add new category to Category (if applicable)
        IF categoryID IS NULL THEN
			SELECT MAX(id) + 1 INTO categoryID FROM Category;
			INSERT INTO Category VALUES(categoryID, carCategory);
		END IF;
        
        -- update relationship tables
        INSERT INTO category_of_car VALUES(carID, categoryID);
        INSERT INTO pickup_car_from VALUES(carID, pickupID);
        
        -- return success message
		SET message = CONCAT('Success : carID = ', carID, ' category ID = ', categoryID, ' pickupLocationID = ', pickupID);
    END IF;
END$$
DELIMITER ;

/*
CALL add_car('Chevrole', 'Maliboo', 2021, 'zz', 'bb', @message);
SELECT @message;

SELECT * FROM PickupLocation WHERE address = 'zz';
SELECT * FROM Category WHERE name = 'bb';
SELECT * FROM Category, category_of_car WHERE name = 'bb' AND Category.id = category_of_car.categoryID;
SELECT * FROM PickupLocation, pickup_car_from WHERE address = 'zz' AND PickupLocation.id = pickup_car_from.pickupLocationID; */

DROP PROCEDURE IF EXISTS add_pickup_location;
DELIMITER $$
CREATE PROCEDURE add_pickup_location(
	IN address varchar(200), phoneNumber varchar(20),
    OUT message varchar(100)
)
BEGIN
	-- declare and assign helper variables for IDs
    DECLARE pickupID varchar(10);
    SELECT MAX(nextID) INTO pickupID FROM HelperPickupID;
    
    -- increment helper table of ID
	SET SQL_SAFE_UPDATES = 0;
	UPDATE HelperPickupID SET nextID = nextID + 1;
	SET SQL_SAFE_UPDATES = 1;
    
    -- insert location into PickupLocation table
	INSERT INTO PickupLocation VALUES(pickupID, address, phoneNumber);

	-- return success message
	SET message = CONCAT('Success : pickupLocationID = ', pickupID);
END$$
DELIMITER ;

/*
CALL add_pickup_location('blah', '', @message);
SELECT @message;

SELECT * FROM PickupLocation WHERE address = 'blah';*/

DROP PROCEDURE IF EXISTS add_car_xml;
DELIMITER $$
CREATE PROCEDURE add_car_xml(
	IN carMake varchar(100), carModel varchar(100), carYear integer, carCategory varchar(100), id integer
)
BEGIN
	-- declare and assign helper variables for IDs
	DECLARE carID varchar(10);
    DECLARE nextMax integer;
    DECLARE categoryID integer;
    SELECT MAX(Cars.id) INTO carID FROM Cars WHERE Cars.make = carMake AND Cars.model = carModel AND Cars.year = carYear;
    SELECT MAX(Category.id) INTO categoryID FROM Category WHERE Category.name = carCategory;
    
	IF carID IS NOT NULL THEN
		--  insert error message, duplicate car into error msg table
        INSERT INTO HelperXMLDuplicatesCar VALUES(CONCAT('Duplicate <vehicle> : ', carMake, ' ', carModel, ' ', carYear));
        
	ELSEIF NOT EXISTS(SELECT Cars.id FROM Cars WHERE Cars.id = id) THEN
        -- add new car to Cars 
        INSERT INTO Cars VALUES(id, carModel, carMake, carYear);
        
        -- update Helper ID table (if necessary) to reflect any new maximums
        SELECT MAX(nextID) INTO nextMax FROM HelperCarsID;
        IF id >= nextMax THEN
			SET SQL_SAFE_UPDATES = 0;
            UPDATE HelperCarsID SET nextID = id + 1;
            SET SQL_SAFE_UPDATES = 1;
        END IF;
        
        -- add new category to Category (if applicable)
        IF categoryID IS NULL THEN
			SELECT MAX(Category.id) + 1 INTO categoryID FROM Category;
			INSERT INTO Category VALUES(categoryID, carCategory);
		END IF;
        
        -- update Category relationship tables
        INSERT INTO category_of_car VALUES(id, categoryID);
        
	END IF;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS add_pickup_xml;
DELIMITER $$
CREATE PROCEDURE add_pickup_xml(
	IN locAddress varchar(200), locPhone varchar(20), locId integer
)
BEGIN
	-- declare and assign helper variables for IDs
    DECLARE nextMax integer;
	DECLARE pickupID varchar(10);
    SELECT MAX(PickupLocation.id) INTO pickupID FROM PickupLocation WHERE PickupLocation.address = locAddress;
    
	IF pickupID IS NOT NULL THEN
		--  insert error message, duplicate pickup location into error msg table
        INSERT INTO HelperXMLDuplicatesLoc VALUES(CONCAT('Duplicate <pickup  location> : ', locAddress));
        
	ELSEIF NOT EXISTS(SELECT PickupLocation.id FROM PickupLocation WHERE PickupLocation.id = CAST(locId AS CHAR(10))) THEN
        -- add new pickup location to PickupLocation 
        INSERT INTO PickupLocation VALUES(locId, locAddress, locPhone);
        
        -- update Helper ID table (if necessary) to reflect any new maximums
        SELECT MAX(nextID) INTO nextMax FROM HelperPickupID;
        IF locId >= nextMax THEN
			SET SQL_SAFE_UPDATES = 0;
            UPDATE HelperPickupID SET nextID = locId + 1;
            SET SQL_SAFE_UPDATES = 1;
        END IF;
	END IF;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS find_mapping_xml_errors;
DELIMITER $$
CREATE PROCEDURE find_mapping_xml_errors(
	IN locId integer, carsId integer
)
BEGIN
	DECLARE locIdstr VARCHAR(10);
    DECLARE carsIdstr VARCHAR(10);
    SET locIdstr = CAST(locId AS CHAR(10));
    SET carsIdstr = CAST(carsId AS CHAR(10));
    
	IF EXISTS(SELECT * FROM pickup_car_from WHERE pickup_car_from.carID = carsIdstr AND pickup_car_from.pickupLocationID = locIdstr) THEN
		--  insert error message, duplicate pickup location into error msg table
        INSERT INTO HelperXMLDuplicatesMap VALUES(CONCAT('Duplicate <pickup_car_from> : carID(', carsId, '), pickupLocationID(', locId, ')'));
        
	ELSEIF NOT EXISTS(SELECT PickupLocation.id FROM PickupLocation WHERE PickupLocation.id = locIdstr) OR 
		   NOT EXISTS(SELECT Cars.id FROM Cars WHERE Cars.id = carsIdstr) THEN
        -- add new pickup location to PickupLocation 
        INSERT INTO HelperXMLDuplicatesMap VALUES(CONCAT('Foreign key constraint violation <pickup_car_from> : carID(', carsId, '), pickupLocationID(', locId, ')'));
	END IF;
END$$
DELIMITER ;
