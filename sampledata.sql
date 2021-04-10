USE testing;
INSERT INTO Cars VALUES('1', 'Q3', 'Audi', '2020'),
('2', 'Malibu', 'Chevrolet', '2020'),
('3', 'Escalade ESV', 'Cadillac', '2020'),
('4', 'Corvette', 'Chevrolet', '2020');
-- Category is auto increment
INSERT INTO Category(name) VALUES('pickup'),
('wagon'),
('van'),
('coupe'),
('sedan'),
('hatchback'),
('convertible'),
('SUV');
INSERT INTO PickupLocation VALUES('a', '626 Ash Street, Cumminsville, Virgin Islands, 1864', '+1 (915) 417-3128'),
('b', '903 Jamison Lane, Garfield, Illinois, 2213', '+1 (872) 444-3208'),
('c', '144 Beekman Place, Gila, Massachusetts, 7971', '+1 (822) 599-2617'),
('d', '554 Boynton Place, Castleton, Arkansas, 6715', '+1 (815) 463-2639'),
('e', '763 Kings Hwy, Wilsonia, Mississippi, 4202', '+1 (812) 546-3998');
INSERT INTO Ratings VALUES('1', 4.9, 92109),
('2', 4.9, 4530),
('3', 3.8, 67001),
('4', 2.6, 39024);
INSERT INTO pickup_car_from VALUES('1', 'a'),
('1', 'c'),
('1', 'e'),
('2', 'a'),
('2', 'd'),
('3', 'b'),
('4', 'b');
INSERT INTO category_of_car VALUES('1', 1),
('2', 3),
('3', 5),
('4', 7);
INSERT INTO CreditCards VALUES('490001', 'James', 'Brown', '2007/09/20'),
('490002', 'Margaret', 'Black', '2006/05/20'),
('490003', 'Keith', 'Black', '2006/06/25');
INSERT INTO Customers VALUES(490001, 'James', 'Brown', '490001', '530 White Ave., Los Angeles, CA 91701', 'jbrown@ics185.edu', 'keyboard'),
(490002, 'John', 'Black', '490002', '531 Green Ave., Anaheim, CA 92456', 'jblack@ics185.edu', 'paper'),
(490003, 'Keith', 'White', '490003', '532 Yale Ave., Irvine, CA 92617', 'kwhite@ics185.edu', 'book');
INSERT INTO Reservations VALUES(1,  '2005/01/05', '2005/1/25', 490001, '1'),
(2,  '2005/01/12', '2005/1/31', 490002, '2'),
(3,  '2005/01/09', '2005/1/10', 490003, '3');
