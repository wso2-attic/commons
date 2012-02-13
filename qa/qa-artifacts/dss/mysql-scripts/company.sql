DROP DATABASE Company;
CREATE DATABASE Company;
USE Company; 


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



CREATE TABLE Employee
(
ID int NOT NULL AUTO_INCREMENT,
LName varchar(100) NOT NULL,
FName varchar(100) NOT NULL,
Address varchar(255),
Dep_id int,
PRIMARY KEY (Id),
FOREIGN KEY (Dep_id) REFERENCES Department(Dep_ID)
);

INSERT INTO Employee(LName, FName, Address, Dep_id) 
VALUES('Perera','Thushara','Address1',1);
INSERT INTO Employee(LName, FName, Address, Dep_id) 
VALUES('Liyanage','Tharinda','No27,1st lane',2);
INSERT INTO Employee(LName, FName, Address, Dep_id) 
VALUES('Amarasiri','Evanthika','No28,2nd lane',3);
INSERT INTO Employee(LName, FName, Address, Dep_id) 
VALUES('Peiris','Thushara','No29,1st lane',2);
INSERT INTO Employee(LName, FName, Address, Dep_id) 
VALUES('Jayasuriya','Sanath','No26,1st lane',1);




CREATE TABLE Project
(
P_ID int NOT NULL AUTO_INCREMENT,
P_Name varchar(50) NOT NULL,
Client_Name varchar(50),
Description varchar(255),
Manager int NOT NULL,
PRIMARY KEY (P_ID),
FOREIGN KEY (Manager) REFERENCES Employee(ID)
);

INSERT INTO Project(P_Name, Client_Name, Description,Manager)
VALUES('project1', 'client1', 'Descreiption sssssssssss dddddddd ddddddddddddd aaaaaaaaa ddddddd',2);
INSERT INTO Project(P_Name, Client_Name, Description,Manager)
VALUES('project2', 'client2', 'Descreiption ssssss fffff aaaaaa eeeeeeee rrrrrrr ttttttttaaaaa e',4);
INSERT INTO Project(P_Name, Client_Name, Description,Manager)
VALUES('project3', 'client3', 'escreiption ssssss fffff aaaaaa eeeeeeee rrrrrrr ttttttttaaaaa e',5);





CREATE TABLE Vehicle
(
Reg_NO varchar(10) NOT NULL,
Model_name varchar(35) NOT NULL,
AllocatedTo int,
PRIMARY KEY (Reg_NO),
FOREIGN KEY (AllocatedTo) REFERENCES Employee(ID)
);


INSERT INTO Vehicle(Reg_NO,model_name,AllocatedTo)
VALUES('KA1523','Nissan Sunny', 2 );
INSERT INTO Vehicle(Reg_NO,model_name,AllocatedTo)
VALUES('FJ1532','Toyota Axio', 4 );
INSERT INTO Vehicle(Reg_NO,model_name,AllocatedTo)
VALUES('SA2243','Toyota Corolla', 5 );
INSERT INTO Vehicle(Reg_NO,model_name,AllocatedTo)
VALUES('KH2435','Nissan Sunny', NULL);
INSERT INTO Vehicle(Reg_NO,model_name,AllocatedTo)
VALUES('PM7823','Toyota Vios', NULL);


SELECT * FROM Employee;

SELECT * FROM Department;

SELECT * FROM Project;

SELECT * FROM Vehicle;

 
