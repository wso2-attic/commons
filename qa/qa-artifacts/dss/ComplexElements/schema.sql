DROP DATABASE IF EXISTS ComplexElemTest;
CREATE DATABASE ComplexElemTest;
USE ComplexElemTest;

CREATE TABLE Employee(
rowid INT NOT NULL AUTO_INCREMENT,
Name VARCHAR(100),
Age TINYINT,
Telephone VARCHAR(10),
Street VARCHAR(100),
StreetLine2 VARCHAR(100),
City VARCHAR(30),
PostalCode MEDIUMINT,
PRIMARY KEY (rowid)
);

INSERT INTO Employee(Name, Age, Telephone, Street, StreetLine2, City, PostalCode) VALUES('emp1', 31, '0112345678', 'streetline1', 'streetline2', 'Colombo', 09134);
INSERT INTO Employee(Name, Age, Telephone, Street, StreetLine2, City, PostalCode) VALUES('emp2', 32, '0112344678', 'streetline1', 'streetline2', 'Colombo', 09134);
INSERT INTO Employee(Name, Age, Telephone, Street, StreetLine2, City, PostalCode) VALUES('emp3', 33, '0112345578', 'streetline1', 'streetline2', 'Colombo', 09134);
INSERT INTO Employee(Name, Age, Telephone, Street, StreetLine2, City, PostalCode) VALUES('emp4', 34, '0112345668', 'streetline1', 'streetline2', 'Colombo', 09134);
INSERT INTO Employee(Name, Age, Telephone, Street, StreetLine2, City, PostalCode) VALUES('emp5', 35, '0112345678', 'streetline1', 'streetline2', 'Colombo', 09134);
