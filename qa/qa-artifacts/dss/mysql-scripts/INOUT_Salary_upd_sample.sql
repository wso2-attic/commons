DROP DATABASE IF EXISTS DATASERVICE_SAMPLE1;
CREATE DATABASE DATASERVICE_SAMPLE1;
GRANT ALL ON DATASERVICE_SAMPLE1.* TO 'root'@'localhost' IDENTIFIED BY 'root';

USE DATASERVICE_SAMPLE1;

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

Select * from Salary;
