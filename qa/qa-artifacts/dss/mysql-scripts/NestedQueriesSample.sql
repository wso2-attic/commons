DROP DATABASE IF EXISTS NQ_SAMPLE;
CREATE DATABASE NQ_SAMPLE;
GRANT ALL ON NQ_SAMPLE.* TO 'root'@'localhost' IDENTIFIED BY 'root';

USE NQ_SAMPLE;

DROP TABLE IF EXISTS Employees;

CREATE TABLE Employees(
        employeeNumber INTEGER,
        lastName VARCHAR(50),
        firstName VARCHAR(50),
        extension VARCHAR(10),
        email VARCHAR(100),
        officeCode VARCHAR(10),
        reportsTo INTEGER,
        jobTitle VARCHAR(50)
);


insert into Employees values (1002,'Murphy','Diane','x5800','dmurphy@classicmodelcars.com','1',null,'President');
insert into Employees values (1056,'Patterson','Mary','x4611','mpatterso@classicmodelcars.com','1',1002,'VP Sales');
insert into Employees values (1076,'Firrelli','Jeff','x9273','jfirrelli@classicmodelcars.com','1',1002,'VP Marketing');
insert into Employees values (1088,'Patterson','William','x4871','wpatterson@classicmodelcars.com','6',1056,'Sales Manager (APAC)');
insert into Employees values (1102,'Bondur','Gerard','x5408','gbondur@classicmodelcars.com','4',1056,'Sale Manager (EMEA)');
insert into Employees values (1143,'Bow','Anthony','x5428','abow@classicmodelcars.com','1',1056,'Sales Manager (NA)');
insert into Employees values (1165,'Jennings','Leslie','x3291','ljennings@classicmodelcars.com','1',1143,'Sales Rep');


DROP TABLE IF EXISTS  Salary;

CREATE TABLE Salary(
        employeeNumber INTEGER,
        salary DOUBLE,
        lastRevisedDate DATE
);

INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1002,13000,'2007/11/30');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1056,30000,'2007/01/20');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1076,17500,'2008/01/01');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1088,7000,'2007/05/20');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1102,25000,'2006/12/01');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1143,40500,'2006/03/20');
INSERT into Salary (employeeNumber,salary,lastRevisedDate) values (1165,12000,'2007/02/01');
