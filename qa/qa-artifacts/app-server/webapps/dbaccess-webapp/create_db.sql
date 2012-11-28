CREATE TABLE Department
(
Dep_ID int NOT NULL AUTO_INCREMENT,
Dep_Name varchar(40) NOT NULL,
Location varchar(50),
PRIMARY KEY (Dep_ID)
);

INSERT INTO Department(Dep_Name, Location)
VALUES('Dep1', '1stFloor');
INSERT INTO Department(Dep_Name, Location)
VALUES('Dep2', '2ndFloor');
INSERT INTO Department(Dep_Name, Location)
VALUES('Dep3', '3rdFloor');
